package org.slizaa.scanner.core.classpathscanner;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IClasspathScanner {

  /**
   * <p>
   * </p>
   *
   * @param clazz
   * @param processor
   * @return
   */
  IClasspathScanner matchClassesWithAnnotation(Class<?> clazz, IClassAnnotationMatchHandler processor);

  /**
   * <p>
   * </p>
   *
   */
  void scan();
}