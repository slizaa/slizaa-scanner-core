package org.slizaa.scanner.core.api.graphdb;

import java.io.File;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IGraphDbFactory {

  /**
   * <p>
   * </p>
   *
   * @param port
   * @param storeDir
   * @return
   */
  IGraphDb createGraphDb(int port, File storeDir);

  /**
   * @param port
   * @param storeDir
   * @param dbExtensions
   * @return
   */
  IGraphDb createGraphDb(int port, File storeDir, List<Class<?>> dbExtensions);

  /**
   * <p>
   * </p>
   *
   * @param port
   * @param storeDir
   * @param userObject
   * @return
   */
  <T> IGraphDb createGraphDb(int port, File storeDir, T userObject);

  /**
   * @param port
   * @param storeDir
   * @param userObject
   * @param dbExtensions
   * @return
   */
  <T> IGraphDb createGraphDb(int port, File storeDir, T userObject, List<Class<?>> dbExtensions);
}
