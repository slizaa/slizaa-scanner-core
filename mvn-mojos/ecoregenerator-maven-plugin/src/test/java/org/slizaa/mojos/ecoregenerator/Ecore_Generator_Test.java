package org.slizaa.mojos.ecoregenerator;

import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Ecore_Generator_Test extends AbstractEcoreGeneratorTest {

  /**
   * <p>
   * Creates a new instance of type {@link Ecore_Generator_Test}.
   * </p>
   */
  public Ecore_Generator_Test() {
    super("ecore-generator");
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Test
  public void test() throws Exception {

    //
    getEcoreGeneratorMojo().execute();

    // @formatter:off
    TestResources.assertDirectoryContents(getBasedir(), 
        "pom.xml", 
        "src/", 
        "src\\main/", 
        "src\\main\\resources/",
        "src\\main\\resources\\model/", 
        "src\\main\\resources\\model\\hierarchicalgraph.ecore",
        "src\\main\\resources\\model\\hierarchicalgraph.genmodel", 
        "target/", 
        "target\\workspace/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\META-INF/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\META-INF\\MANIFEST.MF",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\build.properties",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\plugin.properties",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\plugin.xml",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\AbstractHGDependency.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\DefaultDependencySource.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\DefaultNodeSource.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\HGAggregatedDependency.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\HGCoreDependency.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\HGNode.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\HGProxyDependency.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\HGRootNode.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\HierarchicalgraphFactory.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\HierarchicalgraphPackage.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\IDependencySource.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\INodeSource.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\SourceOrTarget.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\AbstractHGDependencyImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\DefaultDependencySourceImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\DefaultNodeSourceImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\HGAggregatedDependencyImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\HGCoreDependencyImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\HGNodeImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\HGProxyDependencyImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\HGRootNodeImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\HierarchicalgraphFactoryImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\HierarchicalgraphPackageImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\IdentifierToNodeMapImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\NodeToCoreDependenciesMapImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\NodeToCoreDependencyMapImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\StringToObjectMapImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\impl\\StringToStringMapImpl.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\util/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\util\\HierarchicalgraphAdapterFactory.java",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\src-gen\\org\\slizaa\\hierarchicalgraph\\util\\HierarchicalgraphSwitch.java");
    // @formatter:on
  }
}
