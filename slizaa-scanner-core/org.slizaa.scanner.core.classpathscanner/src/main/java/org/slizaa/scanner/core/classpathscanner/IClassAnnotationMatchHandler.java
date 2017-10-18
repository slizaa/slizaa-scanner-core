package org.slizaa.scanner.core.classpathscanner;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@FunctionalInterface
public interface IClassAnnotationMatchHandler {

  /**
   * <p>
   * </p>
   *
   * @param codeSource
   * @param classesWithAnnotation
   */
  void processMatch(Object codeSource, List<Class<?>> classesWithAnnotation);
}
