package org.slizaa.scanner.core.classpathscanner;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IOSGiBundleClasspathScannerService {

  /**
   * <p>
   * </p>
   *
   * @param annotation
   * @return
   */
  List<Object> getExtensionsWithClassAnnotation(Class<? extends Annotation> annotationType);
}
