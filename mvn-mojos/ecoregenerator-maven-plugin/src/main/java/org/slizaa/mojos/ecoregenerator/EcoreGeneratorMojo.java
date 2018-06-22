package org.slizaa.mojos.ecoregenerator;

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.PathTool;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.emf.codegen.ecore.generator.Generator;
import org.eclipse.emf.codegen.ecore.generator.GeneratorAdapterFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenModelGeneratorAdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Mojo(name = "generateFromEcore", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class EcoreGeneratorMojo extends AbstractMojo {

  /** - */
  private static final String     WORKSPACE_NAME = "workspace";

  /** - */
  @Component
  private RepositorySystem        repoSystem;

  /** - */
  @Parameter(defaultValue = "${repositorySystemSession}", readonly = true, required = true)
  private RepositorySystemSession repoSession;

  /** - */
  @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true, required = true)
  private List<RemoteRepository>  repositories;

  /** - */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject            project;

  /** - */
  @Parameter(property = "genModel", required = true)
  private String                  genModel;

  /** - */
  @Parameter(property = "ecoreModel", required = true)
  private String                  ecoreModel;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    // compute the workspace directory...
    String workspaceDir = this.project.getBuild().getDirectory() + File.separator + WORKSPACE_NAME + File.separator;

    // populate with referenced models
    for (final Artifact artifact : this.project.getArtifacts()) {
      ExtractModelDefinitions.extractModelDefinitions(artifact.getFile(), workspaceDir);
    }

    // we have to register xmi resource factories for '*.genmodel' and '*.ecore' files
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("genmodel", new XMIResourceFactoryImpl());
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());

    // create the resource set and register the ecore and genmodel packages
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
    resourceSet.getPackageRegistry().put(GenModelPackage.eNS_URI, GenModelPackage.eINSTANCE);

    // ... and add it in the URI map
    resourceSet.getURIConverter().getURIMap().put(URI.createURI("platform:/resource/"),
        URI.createFileURI(workspaceDir));

    try {

      // compute the
      String relativeFilePath = PathTool.getRelativeFilePath(new File(System.getProperty("user.dir")).getAbsolutePath(),
          this.project.getBasedir().getAbsolutePath());

      //
      String relativeGenModelPath = relativeFilePath.isEmpty() ? this.genModel
          : relativeFilePath + File.separator + this.genModel;
      String relativeEcoreModelPath = relativeFilePath.isEmpty() ? this.ecoreModel
          : relativeFilePath + File.separator + this.ecoreModel;

      // load the gen model
      Resource hierarchicalGraphGenModelResource = resourceSet
          .getResource(URI.createFileURI(new File(relativeGenModelPath).getAbsolutePath()), true);
      hierarchicalGraphGenModelResource.load(null);

      // load the ecore model
      Resource reportDesignerEcoreResource = resourceSet
          .getResource(URI.createFileURI(new File(relativeEcoreModelPath).getAbsolutePath()), true);
      reportDesignerEcoreResource.load(null);

      //
      if (hierarchicalGraphGenModelResource.getContents().size() != 1) {
        throw new MojoExecutionException("Specified gen model file does not contain a valid gen model.");
      }

      //
      else {

        // get the gen model
        GenModel genModel = (GenModel) hierarchicalGraphGenModelResource.getContents().get(0);

        // Globally register the default generator adapter factory for
        // GenModel elements (only needed in standalone).
        GeneratorAdapterFactory.Descriptor.Registry.INSTANCE.addDescriptor(GenModelPackage.eNS_URI,
            GenModelGeneratorAdapterFactory.DESCRIPTOR);

        // Create the generator and set the model-level input object.
        Generator generator = new Generator();
        generator.setInput(genModel);
        genModel.setCanGenerate(true);

        // Generator model code.
        generator.generate(genModel, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE,
            new MonitorToMavenLogAdapter(this.getLog()));

        // add the model directory
        String modelDirectory = new File(this.project.getBuild().getDirectory()).getName() + File.separator
            + WORKSPACE_NAME + genModel.getModelDirectory();
        getLog().info(String.format("Adding model directory '%s' to compile source root...", modelDirectory));
        this.project.addCompileSourceRoot(modelDirectory);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}