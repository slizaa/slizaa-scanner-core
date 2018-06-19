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
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Mojo(name = "generateFromEcore", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class EcoreGeneratorMojo extends AbstractMojo {

  /** - */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    // we have to register the resource factories for '*.genmodel' and '*.ecore' files
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("genmodel", new XMIResourceFactoryImpl());
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());

    // create the URL converter
    URIConverter converter = new ExtensibleURIConverterImpl();

    // create the resource set and register the ecore and genmodel packages
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
    resourceSet.getPackageRegistry().put(GenModelPackage.eNS_URI, GenModelPackage.eINSTANCE);

    // compute the workspace directory
    String workspaceDir = project.getBasedir() + File.separator + project.getBuild().getDirectory() + File.separator
        + "workspace";

    new File(workspaceDir).mkdirs();
    
    //
    URI workspaceUri = URI.createFileURI(workspaceDir);
    System.out.println(workspaceUri);
    resourceSet.getURIConverter().getURIMap().put(URI.createURI("platform:/resource/"), workspaceUri);

    try {

      //
      String relativeFilePath = PathTool.getRelativeFilePath(new File(System.getProperty("user.dir")).getAbsolutePath(),
          project.getBasedir().getAbsolutePath());

      //
      String hierarchicalGraphGenModelURI = relativeFilePath + File.separator
          + "src/main/resources/model/hierarchicalgraph.genmodel";
      String hierarchicalGraphEcoreModelURI = relativeFilePath + File.separator
          + "src/main/resources/model/hierarchicalgraph.ecore";

      //
      System.out.println("relativeFilePath: " + relativeFilePath);
      System.out.println("hierarchicalGraphGenModelURI: " + hierarchicalGraphGenModelURI);
      System.out.println("hierarchicalGraphEcoreModelURI: " + hierarchicalGraphEcoreModelURI);

      // load the gen model
      Resource hierarchicalGraphGenModelResource = resourceSet
          .getResource(converter.normalize(URI.createURI(hierarchicalGraphGenModelURI)), true);
      hierarchicalGraphGenModelResource.load(null);

      // load the ecore model
      Resource reportDesignerEcoreResource = resourceSet
          .getResource(converter.normalize(URI.createURI(hierarchicalGraphEcoreModelURI)), true);
      reportDesignerEcoreResource.load(null);

      // // Resource regattaEcoreResource =
      // // resourceSet.getResource(converter.normalize(URI.createURI(regattaEcoreModelURI)),
      // // true);
      // // regattaEcoreResource.load(null);

      //
      if (hierarchicalGraphGenModelResource.getContents().size() != 1) {

        //
        System.out
            .println("Resource has " + hierarchicalGraphGenModelResource.getContents().size() + " loaded objects");
      }

      //
      else {

        //
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
        generator.generate(genModel, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE, new BasicMonitor.Printing(System.out));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}