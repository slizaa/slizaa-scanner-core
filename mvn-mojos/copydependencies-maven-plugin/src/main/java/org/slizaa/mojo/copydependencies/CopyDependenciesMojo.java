package org.slizaa.mojo.copydependencies;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResult;

/**
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Mojo(name = "copyDependencies", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class CopyDependenciesMojo extends AbstractMojo {

  /**
   * The entry point to Maven Artifact Resolver, i.e. the component doing all the work.
   */
  @Component
  private RepositorySystem        repoSystem;

  /** - */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject            project;

  /**
   * The current repository/network configuration of Maven.
   */
  @Parameter(defaultValue = "${repositorySystemSession}", readonly = true, required = true)
  private RepositorySystemSession repoSession;

  /**
   * The project's remote repositories to use for the resolution.
   */
  @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true, required = true)
  private List<RemoteRepository>  remoteRepos;

  /**
   * * The target directory
   */
  @Parameter(defaultValue = "${project.build.directory}/libs", property = "targetDir", required = true)
  private String                  targetDirectory;

  /**
   *
   */
  @Parameter(property = "coords", required = true)
  private List<String>            coords;

  @Parameter(property = "excludeGroupIds", required = false)
  private String                  excludeGroupIds;

  @Parameter(property = "excludeArtifactIds", required = false)
  private String                  excludeArtifactIds;

  private List<String>            excludeGroupIdsList    = Collections.emptyList();

  private List<String>            excludeArtifactIdsList = Collections.emptyList();

  /**
   * @return
   */
  public RepositorySystem getRepoSystem() {
    return repoSystem;
  }

  /**
   * @return
   */
  public RepositorySystemSession getRepoSession() {
    return repoSession;
  }

  /**
   * @return
   */
  public List<RemoteRepository> getRemoteRepos() {
    return remoteRepos;
  }

  /**
   * {@inheritDoc}
   */
  public void execute() throws MojoExecutionException {

    try {

      //
      if (excludeGroupIds != null) {
        excludeGroupIdsList = Arrays.stream(excludeGroupIds.split(",")).map(r -> r.trim()).collect(Collectors.toList());
      }

      //
      if (excludeArtifactIds != null) {
        excludeArtifactIdsList = Arrays.stream(excludeArtifactIds.split(",")).map(r -> r.trim())
            .collect(Collectors.toList());
      }

      //
      CollectRequest collectRequest = new CollectRequest();

      //
      for (String coord : coords) {

        // create the dependency...
        Dependency dependency = new Dependency(new DefaultArtifact(coord), "compile");

        // ..and add it to the request
        collectRequest.addDependency(dependency);
      }

      for (RemoteRepository remoteRepository : remoteRepos) {
        collectRequest.addRepository(remoteRepository);
      }

      //
      DependencyRequest dependencyRequest = new DependencyRequest();
      dependencyRequest.setCollectRequest(collectRequest);
      DependencyResult result = repoSystem.resolveDependencies(repoSession, dependencyRequest);

      // the targetDirectory
      File targetDirectoryAsFile = new File(project.getBasedir(), targetDirectory);

      //
      for (ArtifactResult artifactResult : result.getArtifactResults()) {

        //
        if (!excludeGroupIdsList.contains(artifactResult.getArtifact().getGroupId())
            && !excludeArtifactIdsList.contains(artifactResult.getArtifact().getArtifactId())) {

          //
          FileUtils.copyFile(artifactResult.getArtifact().getFile(),
              new File(targetDirectoryAsFile, artifactResult.getArtifact().getFile().getName()));
        }
      }

    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

}
