package org.slizaa.scanner.core.mvnresolver.implementation;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.util.filter.AndDependencyFilter;
import org.eclipse.aether.util.filter.PatternExclusionsDependencyFilter;
import org.eclipse.aether.util.filter.PatternInclusionsDependencyFilter;
import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.scanner.core.mvnresolver.implementation.internal.ManualRepositorySystemFactory;
import org.slizaa.scanner.core.mvnresolver.implementation.internal.NullConsoleRepositoryListener;
import org.slizaa.scanner.core.mvnresolver.implementation.internal.NullConsoleTransferListener;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MvnResolverServiceImplementation implements IMvnResolverService {

  /** - */
  private static final String     LOCAL_REPO          = System.getProperty("user.home") + File.separator + ".m2"
      + File.separator + "repository";

  /** - */
  private RepositorySystem        _repoSystem;

  /** - */
  private RepositorySystemSession _session;

  /** - */
  private LocalRepository         _localRepository;

  /** - */
  private List<RemoteRepository>  _remoteRepositories = new ArrayList<>();

  /**
   * <p>
   * </p>
   */
  public void initialize() {

    //
    if (this._localRepository == null) {
      setLocalRepository(new File(LOCAL_REPO));
    }

    //
    this._repoSystem = ManualRepositorySystemFactory.newRepositorySystem();

    // new session
    this._session = newRepositorySystemSession(this._repoSystem);

    //
    if (this._remoteRepositories.isEmpty()) {
      this._remoteRepositories.add(newCentralRepository());
    }
  }

  @Override
  public IMvnResolverJob newMvnResolverJob() {
    return new MvnResolverJobImplementation(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public File[] resolve(String... coords) {

    //
    checkNotNull(coords);

    try {

      //
      CollectRequest collectRequest = new CollectRequest();
      for (String coord : coords) {
        collectRequest.addDependency(new Dependency(new DefaultArtifact(coord), "compile"));
      }

      // add remoteRepos
      this._remoteRepositories.forEach(repo -> collectRequest.addRepository(repo));

      DependencyNode node = this._repoSystem.collectDependencies(this._session, collectRequest).getRoot();

      DependencyRequest dependencyRequest = new DependencyRequest();
      dependencyRequest.setRoot(node);

      // DependencyFilter filter = new AndDependencyFilter(new PatternInclusionsDependencyFilter("*:neo4j*"),
      // new PatternExclusionsDependencyFilter("*:*-cypher"));
      //
      // dependencyRequest.setFilter(filter);

      DependencyResult result = this._repoSystem.resolveDependencies(this._session, dependencyRequest);

      //
      return result.getArtifactResults().stream().map(ar -> ar.getArtifact().getFile()).toArray(size -> new File[size]);
    }
    //
    catch (DependencyCollectionException e) {
      throw new RuntimeException(e);
    }
    //
    catch (DependencyResolutionException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param jobImplementation
   * @return
   */
  File[] resolve(MvnResolverJobImplementation job) {

    //
    checkNotNull(job);

    //
    try {

      //
      CollectRequest collectRequest = new CollectRequest();

      // get the coordinates
      for (String coordinate : job.getCoords()) {
        collectRequest.addDependency(new Dependency(new DefaultArtifact(coordinate), "compile"));
      }

      // add remoteRepos
      this._remoteRepositories.forEach(repo -> collectRequest.addRepository(repo));

      DependencyNode node = this._repoSystem.collectDependencies(this._session, collectRequest).getRoot();
      DependencyRequest dependencyRequest = new DependencyRequest();
      dependencyRequest.setRoot(node);

      // construct the filter
      DependencyFilter exclusionFilter = job.getExclusionPatterns().isEmpty() ? null
          : new PatternExclusionsDependencyFilter(job.getExclusionPatterns());

      DependencyFilter inclusionFilter = job.getInclusionPatterns().isEmpty() ? null
          : new PatternInclusionsDependencyFilter(job.getInclusionPatterns());

      if (exclusionFilter != null && inclusionFilter != null) {
        dependencyRequest.setFilter(new AndDependencyFilter(exclusionFilter, inclusionFilter));
      } else if (exclusionFilter != null) {
        dependencyRequest.setFilter(exclusionFilter);
      } else if (inclusionFilter != null) {
        dependencyRequest.setFilter(inclusionFilter);
      }

      //
      DependencyResult result = this._repoSystem.resolveDependencies(this._session, dependencyRequest);

      //
      return result.getArtifactResults().stream().map(ar -> ar.getArtifact().getFile()).toArray(size -> new File[size]);
    }
    //
    catch (DependencyCollectionException e) {
      throw new RuntimeException(e);
    }
    //
    catch (DependencyResolutionException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param localRepository
   */
  void setLocalRepository(File localRepository) {
    this._localRepository = new LocalRepository(checkNotNull(localRepository));
  }

  /**
   * <p>
   * </p>
   *
   * @param id
   * @param url
   */
  void addRemoteRepository(String id, String url) {
    this._remoteRepositories.add(new RemoteRepository.Builder(checkNotNull(id), "default", checkNotNull(url)).build());
  }

  /**
   * <p>
   * </p>
   *
   * @param remoteRepository
   */
  void addRemoteRepository(RemoteRepository remoteRepository) {
    this._remoteRepositories.add(checkNotNull(remoteRepository));
  }

  /**
   * <p>
   * </p>
   *
   * @param system
   * @return
   */
  DefaultRepositorySystemSession newRepositorySystemSession(RepositorySystem system) {
    checkNotNull(system);

    DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

    session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, this._localRepository));
    session.setTransferListener(new NullConsoleTransferListener());
    session.setRepositoryListener(new NullConsoleRepositoryListener());

    // uncomment to generate dirty trees
    // session.setDependencyGraphTransformer( null );

    return session;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  static RemoteRepository newCentralRepository() {
    return new RemoteRepository.Builder("central", "default", "http://repo1.maven.org/maven2").build();
  }
}
