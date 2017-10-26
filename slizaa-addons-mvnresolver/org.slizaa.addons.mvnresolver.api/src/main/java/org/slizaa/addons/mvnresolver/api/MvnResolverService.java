package org.slizaa.addons.mvnresolver.api;

import java.io.File;

public interface MvnResolverService {

  /**
   * <p>
   * </p>
   */
  File[] resolve(String coords);
}
