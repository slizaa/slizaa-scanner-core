package org.slizaa.scanner.core.classpathscanner;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.BiFunction;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IClasspathScannerService {

  /**
   * <p>
   * </p>
   *
   * @param annotation
   * @return
   */
  List<Class<?>> getExtensionsWithClassAnnotation(Class<? extends Annotation> annotationType);

  /**
   * <p>
   * </p>
   *
   * @param annotationType
   * @param targetType
   * @return
   */
  <T> List<Class<T>> getExtensionsWithClassAnnotation(Class<? extends Annotation> annotationType, Class<T> targetType);

  /**
   * <p>
   * </p>
   *
   * @param annotation
   * @return
   */
  List<Class<?>> getExtensionsWithMethodAnnotation(Class<? extends Annotation> annotationType);

  /**
   * <p>
   * </p>
   *
   * @param annotationType
   * @param targetType
   * @return
   */
  <T> List<Class<T>> getExtensionsWithMethodAnnotation(Class<? extends Annotation> annotationType, Class<T> targetType);

  /**
   * <p>
   * </p>
   *
   * @param postfix
   * @param resultType
   * @param transformer
   * @return
   */
  <T> List<T> getFiles(String postfix, Class<T> resultType, BiFunction<String, InputStream, T> transformer);
}
