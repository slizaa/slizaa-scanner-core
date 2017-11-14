package org.slizaa.scanner.core.mvnresolver.api;

import java.io.File;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IMvnResolverServiceFactory {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  MvnResolverServiceFactoryBuilder newMvnResolverService();

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static interface MvnResolverServiceFactoryBuilder {

    /**
     * <p>
     * </p>
     *
     * @param id
     * @param url
     * @return
     */
    MvnResolverServiceFactoryBuilder withRemoteRepository(String id, String url);

    /**
     * <p>
     * </p>
     *
     * @return
     */
    MvnResolverServiceFactoryBuilder withDefaultRemoteRepository();

    /**
     * <p>
     * </p>
     *
     * @param localRepoDirectory
     * @return
     */
    MvnResolverServiceFactoryBuilder withLocalRepository(File localRepoDirectory);

    /**
     * <p>
     * </p>
     *
     * @return
     */
    IMvnResolverService create();
  }
}
