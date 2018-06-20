package org.slizaa.mojos.ecoregenerator;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.junit.Rule;
import org.junit.Test;

public class Ecore_Generator_Test {

  /** - */
  @Rule
  public MojoRule      rule      = new MojoRule();

  /** - */
  @Rule
  public TestResources resources = new TestResources();

  @Test
  public void test() throws Exception {

    // get the test pom file
    File testPom = new File("src/test/projects/ecore-generator/pom.xml").getAbsoluteFile();
    assertNotNull(testPom);

    // create the ecore generator mojo
    EcoreGeneratorMojo mojo = new EcoreGeneratorMojo();
    mojo = (EcoreGeneratorMojo) this.rule.configureMojo(mojo,
        this.rule.extractPluginConfiguration("ecoregenerator-maven-plugin", testPom));

    //
    File basedir = this.resources.getBasedir("ecore-generator");

    // Create the Maven project by hand (...)
    final MavenProject mvnProject = new MavenProject();
    mvnProject.setFile(new File(basedir, "pom.xml"));
    mvnProject.getBuild().setDirectory(new File(basedir, "target").getAbsolutePath());
    this.rule.setVariableValueToObject(mojo, "project", mvnProject);

    //
    mojo.execute();

    //
    TestResources.assertDirectoryContents(basedir, "pom.xml", "src/", "src\\main/", "src\\main\\resources/",
        "src\\main\\resources\\model/", "src\\main\\resources\\model\\hierarchicalgraph.ecore",
        "src\\main\\resources\\model\\hierarchicalgraph.genmodel", "target/", "target\\workspace/",
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
  }
}
