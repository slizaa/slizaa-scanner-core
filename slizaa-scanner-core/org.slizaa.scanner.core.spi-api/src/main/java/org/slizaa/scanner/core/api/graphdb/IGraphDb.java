package org.slizaa.scanner.core.api.graphdb;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IGraphDb extends AutoCloseable {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  int getPort();

  /**
   * <p>
   * </p>
   *
   */
  void shutdown();

  /**
   * <p>
   * </p>
   *
   * @param userObject
   * @return
   */
  <T> T getUserObject(Class<T> userObject);

  /**
   * <p>
   * </p>
   *
   * @param userObject
   * @return
   */
  <T> boolean hasUserObject(Class<T> userObject);
}
