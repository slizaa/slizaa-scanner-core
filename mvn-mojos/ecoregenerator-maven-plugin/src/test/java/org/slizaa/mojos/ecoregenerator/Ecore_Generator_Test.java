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

    File testPom = new File("src/test/projects/ecore-generator/pom.xml").getAbsoluteFile();
    assertNotNull(testPom);
    
    EcoreGeneratorMojo mojo = new EcoreGeneratorMojo();
    mojo = (EcoreGeneratorMojo) rule.configureMojo(mojo,
        rule.extractPluginConfiguration("ecoregenerator-maven-plugin", testPom));

    // Create the Maven project by hand (...)
    final MavenProject mvnProject = new MavenProject();
    mvnProject.setFile(testPom);
    mvnProject.getBuild().setDirectory("target");
    this.rule.setVariableValueToObject(mojo, "project", mvnProject);

    mojo.execute();

    // EcoreGeneratorMojo generatorMojo = findEcoreGeneratorMojo("generateFromEcore");
    // generatorMojo.execute();
  }
}
