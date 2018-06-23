package org.slizaa.mojos.ecoregenerator;

import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Ecore_Generator_2_Test extends AbstractEcoreGeneratorTest {

  /**
   * <p>
   * Creates a new instance of type {@link Ecore_Generator_2_Test}.
   * </p>
   */
  public Ecore_Generator_2_Test() {
    super("ecore-generator-2");
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
        "model/",
        "model\\hierarchicalgraph-neo4j.ecore",
        "model\\hierarchicalgraph-neo4j.genmodel",
        "pom.xml",
        "target/",
        "target\\workspace/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\model/",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\model\\hierarchicalgraph.ecore",
        "target\\workspace\\org.slizaa.hierarchicalgraph.core.model\\model\\hierarchicalgraph.genmodel",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph/",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\META-INF/",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\META-INF\\MANIFEST.MF",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\build.properties",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\plugin.properties",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\plugin.xml",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen/",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org/",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa/",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j/",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph/",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\Neo4JBackedDependencySource.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\Neo4JBackedNodeSource.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\Neo4JBackedRootNodeSource.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\Neo4jHierarchicalgraphFactory.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\Neo4jHierarchicalgraphPackage.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\impl/",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\impl\\Neo4JBackedDependencySourceImpl.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\impl\\Neo4JBackedNodeSourceImpl.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\impl\\Neo4JBackedRootNodeSourceImpl.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\impl\\Neo4jHierarchicalgraphFactoryImpl.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\impl\\Neo4jHierarchicalgraphPackageImpl.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\util/",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\util\\Neo4jHierarchicalgraphAdapterFactory.java",
        "target\\workspace\\org.slizaa.neo4j.hierarchicalgraph\\src-gen\\org\\slizaa\\neo4j\\hierarchicalgraph\\util\\Neo4jHierarchicalgraphSwitch.java");
    // @formatter:on

    // https://stackoverflow.com/questions/9386348/register-ecore-meta-model-programmatically
  }
}
