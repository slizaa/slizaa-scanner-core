package org.slizaa.scanner.core.mvnresolver.implementation;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverServiceFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MvnResolverServiceFactoryImplementation implements IMvnResolverServiceFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public MvnResolverServiceFactoryBuilder newMvnResolverService() {
    return new MvnResolverServiceFactoryBuilderImplementation();
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class MvnResolverServiceFactoryBuilderImplementation implements MvnResolverServiceFactoryBuilder {

    /** - */
    private MvnResolverServiceImplementation _implementation;

    /**
     * <p>
     * Creates a new instance of type {@link MvnResolverServiceFactoryBuilderImplementation}.
     * </p>
     */
    public MvnResolverServiceFactoryBuilderImplementation() {
      _implementation = new MvnResolverServiceImplementation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MvnResolverServiceFactoryBuilder withRemoteRepository(String id, String url) {
      _implementation.addRemoteRepository(id, url);
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MvnResolverServiceFactoryBuilder withDefaultRemoteRepository() {
      _implementation.addRemoteRepository(MvnResolverServiceImplementation.newCentralRepository());
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MvnResolverServiceFactoryBuilder withLocalRepository(File localRepoDirectory) {
      _implementation.setLocalRepository(checkNotNull(localRepoDirectory));
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMvnResolverService create() {
      _implementation.initialize();
      return _implementation;
    }

  }
}
