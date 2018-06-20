package org.slizaa.mojos.ecoregenerator;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.PathTool;
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
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Mojo(name = "generateFromEcore", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class EcoreGeneratorMojo extends AbstractMojo {

  /** - */
  private static final String WORKSPACE_NAME = "workspace";

  /** - */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject        project;

  @Parameter(property = "genModel", required = true)
  private String              genModel;

  @Parameter(property = "ecoreModel", required = true)
  private String              ecoreModel;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    // we have to register xmi resource factories for '*.genmodel' and '*.ecore' files
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("genmodel", new XMIResourceFactoryImpl());
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());

    // create the URL converter
    URIConverter converter = new ExtensibleURIConverterImpl();

    // create the resource set and register the ecore and genmodel packages
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
    resourceSet.getPackageRegistry().put(GenModelPackage.eNS_URI, GenModelPackage.eINSTANCE);

    // compute the workspace directory...
    String workspaceDir = this.project.getBuild().getDirectory() + File.separator + WORKSPACE_NAME + File.separator;

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

      //
      URI relativeGenModelURI = converter.normalize(URI.createURI(relativeGenModelPath));
      URI relativeEcoreModelURI = converter.normalize(URI.createURI(relativeEcoreModelPath));

      // load the gen model
      Resource hierarchicalGraphGenModelResource = resourceSet.getResource(relativeGenModelURI, true);
      hierarchicalGraphGenModelResource.load(null);

      // load the ecore model
      Resource reportDesignerEcoreResource = resourceSet.getResource(relativeEcoreModelURI, true);
      reportDesignerEcoreResource.load(null);

      // TODO: load referenced models
      // Resource referencedEcoreResource =
      // resourceSet.getResource(converter.normalize(URI.createURI(referencedEcoreModelURI)), true);
      // regattaEcoreResource.load(null);

      //
      if (hierarchicalGraphGenModelResource.getContents().size() != 1) {

        // throw new mojo execution exception
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