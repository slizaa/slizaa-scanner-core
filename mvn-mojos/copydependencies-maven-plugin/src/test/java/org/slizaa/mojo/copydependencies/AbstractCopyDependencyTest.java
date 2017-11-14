package org.slizaa.mojo.copydependencies;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.slizaa.mojo.copydependencies.fwk.Booter;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractCopyDependencyTest {

  @Rule
  public MojoRule      rule      = new MojoRule();

  @Rule
  public TestResources resources = new TestResources();

  /** - */
  private String       _projectName;

  /** - */
  private File         _targetDirectory;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractCopyDependencyTest}.
   * </p>
   *
   * @param projectName
   */
  public AbstractCopyDependencyTest(String projectName) {
    _projectName = checkNotNull(projectName);
  }

  @Before
  public void before() throws Exception {

    // check project dir
    File baseDir = this.resources.getBasedir(_projectName);
    assertThat(baseDir).isNotNull();
    assertThat(baseDir.isDirectory()).isTrue();
  }

  /**
   * <p>
   * </p>
   *
   * @param projectName
   * @param goalName
   * @return
   * @throws Exception
   */
  protected CopyDependenciesMojo findCopyDependencyMojo(String goalName) throws Exception {

    // Find the project
    File baseDir = this.resources.getBasedir(_projectName);
    Assert.assertNotNull(baseDir);
    Assert.assertTrue(baseDir.isDirectory());

    File pom = new File(baseDir, "pom.xml");
    CopyDependenciesMojo mojo = (CopyDependenciesMojo) this.rule.lookupMojo(goalName, pom);
    Assert.assertNotNull(mojo);

    // Create the Maven project by hand (...)
    final MavenProject mvnProject = new MavenProject();
    mvnProject.setFile(pom);

    this.rule.setVariableValueToObject(mojo, "project", mvnProject);
    Assert.assertNotNull(this.rule.getVariableValueFromObject(mojo, "project"));

    this.rule.setVariableValueToObject(mojo, "repoSession", Booter.newRepositorySystemSession(mojo.getRepoSystem()));
    Assert.assertNotNull(this.rule.getVariableValueFromObject(mojo, "repoSession"));

    this.rule.setVariableValueToObject(mojo, "remoteRepos",
        Booter.newRepositories(mojo.getRepoSystem(), mojo.getRepoSession()));
    Assert.assertNotNull(this.rule.getVariableValueFromObject(mojo, "remoteRepos"));

    String targetDir = (String) this.rule.getVariableValueFromObject(mojo, "targetDirectory");
    _targetDirectory = new File(baseDir, targetDir);

    return mojo;
  }

  /**
   * <p>
   * </p>
   *
   * @param fileName
   */
  protected List<File> getCopiedFiles() {
    return Arrays.asList(_targetDirectory.listFiles());
  }
}