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
    this._testProjectName = checkNotNull(testProjectName);
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
    this._basedir = this.testResources.getBasedir(this._testProjectName);

    System.out.println(this._basedir);

    // get the test pom file
    File testPom = new File(this._basedir, "pom.xml").getAbsoluteFile();
    assertNotNull(testPom);

    //
    this._ecoreGeneratorMojo = (EcoreGeneratorMojo) this.mojoRule.configureMojo(new EcoreGeneratorMojo(),
        this.mojoRule.extractPluginConfiguration("ecoregenerator-maven-plugin", testPom));

    // Create the Maven project by hand (...)
    final MavenProject mvnProject = new MavenProject();
    mvnProject.setFile(testPom);
    mvnProject.getBuild().setDirectory(new File(this._basedir, "target").getAbsolutePath());
    this.mojoRule.setVariableValueToObject(this._ecoreGeneratorMojo, "project", mvnProject);

    //
    RepositorySystem repositorySystem = ManualRepositorySystemFactory.newRepositorySystem();
    this.mojoRule.setVariableValueToObject(this._ecoreGeneratorMojo, "repoSystem", repositorySystem);
    Assert.assertNotNull(this.mojoRule.getVariableValueFromObject(this._ecoreGeneratorMojo, "repoSystem"));

    //
    DefaultRepositorySystemSession systemSession = Booter.newRepositorySystemSession(repositorySystem);
    this.mojoRule.setVariableValueToObject(this._ecoreGeneratorMojo, "repoSession", systemSession);
    Assert.assertNotNull(this.mojoRule.getVariableValueFromObject(this._ecoreGeneratorMojo, "repoSession"));

    this.mojoRule.setVariableValueToObject(this._ecoreGeneratorMojo, "repositories",
        Booter.newRepositories(repositorySystem, systemSession));
    Assert.assertNotNull(this.mojoRule.getVariableValueFromObject(this._ecoreGeneratorMojo, "repositories"));
  }

  protected MojoRule getMojoRule() {
    return this.mojoRule;
  }

  protected TestResources getTestResources() {
    return this.testResources;
  }

  protected String getTestProjectName() {
    return this._testProjectName;
  }

  protected EcoreGeneratorMojo getEcoreGeneratorMojo() {
    return this._ecoreGeneratorMojo;
  }

  protected File getBasedir() {
    return this._basedir;
  }
}
