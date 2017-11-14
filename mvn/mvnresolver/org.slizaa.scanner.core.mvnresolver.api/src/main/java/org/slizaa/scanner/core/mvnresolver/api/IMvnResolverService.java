package org.slizaa.scanner.core.mvnresolver.api;

import java.io.File;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IMvnResolverService {

  /**
   * <p>
   * </p>
   *
   * @param coords
   * @return
   */
  File[] resolve(String coords);
}
