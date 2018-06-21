package org.slizaa.mojos.ecoregenerator;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.slizaa.mojos.ecoregenerator.fwk.Booter;
import org.slizaa.mojos.ecoregenerator.fwk.ManualRepositorySystemFactory;

public class AbstractEcoreGeneratorTest {

  /** - */
  @Rule
  public MojoRule            mojoRule      = new MojoRule();

  /** - */
  @Rule
  public TestResources       testResources = new TestResources();

  /** - */
  private String             _testProjectName;

  /** - */
  private EcoreGeneratorMojo _ecoreGeneratorMojo;

  /** - */
  private File               _basedir;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractEcoreGeneratorTest}.
   * </p>
   *
   * @param testProjectName
   */
  public AbstractEcoreGeneratorTest(String testProjectName) {
    _testProjectName = checkNotNull(testProjectName);
  }

  /**
   * <p>
   * </p>
   *
   * @throws Exception
   */
  @Before
  public void setup() throws Exception {

    //
    _basedir = this.testResources.getBasedir(_testProjectName);

    // get the test pom file
    File testPom = new File(_basedir, "pom.xml").getAbsoluteFile();
    assertNotNull(testPom);

    //
    _ecoreGeneratorMojo = (EcoreGeneratorMojo) this.mojoRule.configureMojo(new EcoreGeneratorMojo(),
        this.mojoRule.extractPluginConfiguration("ecoregenerator-maven-plugin", testPom));

    // Create the Maven project by hand (...)
    final MavenProject mvnProject = new MavenProject();
    mvnProject.setFile(testPom);
    mvnProject.getBuild().setDirectory(new File(_basedir, "target").getAbsolutePath());
    this.mojoRule.setVariableValueToObject(_ecoreGeneratorMojo, "project", mvnProject);

    //
    RepositorySystem repositorySystem = ManualRepositorySystemFactory.newRepositorySystem();
    this.mojoRule.setVariableValueToObject(_ecoreGeneratorMojo, "repoSystem", repositorySystem);
    Assert.assertNotNull(this.mojoRule.getVariableValueFromObject(_ecoreGeneratorMojo, "repoSystem"));

    //
    DefaultRepositorySystemSession systemSession = Booter.newRepositorySystemSession(repositorySystem);
    this.mojoRule.setVariableValueToObject(_ecoreGeneratorMojo, "repoSession", systemSession);
    Assert.assertNotNull(this.mojoRule.getVariableValueFromObject(_ecoreGeneratorMojo, "repoSession"));

    this.mojoRule.setVariableValueToObject(_ecoreGeneratorMojo, "repositories",
        Booter.newRepositories(repositorySystem, systemSession));
    Assert.assertNotNull(this.mojoRule.getVariableValueFromObject(_ecoreGeneratorMojo, "repositories"));
  }

  protected MojoRule getMojoRule() {
    return mojoRule;
  }

  protected TestResources getTestResources() {
    return testResources;
  }

  protected String getTestProjectName() {
    return _testProjectName;
  }

  protected EcoreGeneratorMojo getEcoreGeneratorMojo() {
    return _ecoreGeneratorMojo;
  }

  protected File getBasedir() {
    return _basedir;
  }
}
