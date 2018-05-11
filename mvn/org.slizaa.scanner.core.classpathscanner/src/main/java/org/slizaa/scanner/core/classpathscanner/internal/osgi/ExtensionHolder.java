package org.slizaa.scanner.core.classpathscanner.internal.osgi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExtensionHolder {

  /** - */
  private Map<Class<? extends Annotation>, List<Class<?>>> _extensionsWithClassAnnotation;

  /** - */
  private Map<Class<? extends Annotation>, List<Class<?>>> _extensionsWithMethodAnnotation;

  /** - */
  private Map<String, List<?>>                             _fileExtensions;

  /** - */
  private Bundle                                           _bundle;

  /** - */
  private IClasspathScannerFactory                         _classpathScannerFactory;

  /**
   * <p>
   * Creates a new instance of type {@link ExtensionHolder}.
   * </p>
   *
   * @param classpathScannerFactory
   */
  public ExtensionHolder(Bundle bundle, IClasspathScannerFactory classpathScannerFactory) {
    this._bundle = checkNotNull(bundle);
    this._classpathScannerFactory = checkNotNull(classpathScannerFactory);
    this._extensionsWithClassAnnotation = new HashMap<>();
    this._extensionsWithMethodAnnotation = new HashMap<>();
    this._fileExtensions = new HashMap<>();
  }

  /**
   * <p>
   * </p>
   *
   * @param annotationType
   * @return
   */
  public List<Class<?>> getExtensionsWithMethodAnnotation(Class<? extends Annotation> annotationType) {

    //
    if (!this._extensionsWithMethodAnnotation.containsKey(checkNotNull(annotationType))) {

      // scan the bundle
      this._classpathScannerFactory

          //
          .createScanner(this._bundle)

          //
          .matchClassesWithMethodAnnotation(annotationType, (b, exts) -> {
            this._extensionsWithMethodAnnotation.put(annotationType, exts);
          })

          //
          .scan();
    }

    //
    return this._extensionsWithMethodAnnotation.get(annotationType);
  }

  /**
   * <p>
   * </p>
   *
   * @param annotationType
   * @return
   */
  public List<Class<?>> getExtensionsWithClassAnnotation(Class<? extends Annotation> annotationType) {

    //
    if (!this._extensionsWithClassAnnotation.containsKey(checkNotNull(annotationType))) {

      // scan the bundle
      this._classpathScannerFactory

          //
          .createScanner(this._bundle)

          //
          .matchClassesWithAnnotation(annotationType, (b, exts) -> {
            this._extensionsWithClassAnnotation.put(annotationType, exts);
          })

          //
          .scan();
    }

    //
    return this._extensionsWithClassAnnotation.get(annotationType);
  }

  /**
   * <p>
   * </p>
   *
   * @param resultType
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> List<T> getFiles(String postfix, Class<T> resultType, BiFunction<String, InputStream, T> transformer) {

    //
    if (!this._fileExtensions.containsKey(checkNotNull(postfix))) {

      // scan the bundle
      this._classpathScannerFactory

          //
          .createScanner(this._bundle)

          //
          .matchFiles(postfix, (relativePath, inputStream, lengthBytes) -> transformer.apply(relativePath, inputStream),
              (b, contentList) -> this._fileExtensions.put(postfix, contentList))

          //
          .scan();
    }

    //
    return this._fileExtensions.get(postfix).stream().filter(element -> resultType.isAssignableFrom(element.getClass()))
        .map(element -> (T) element).collect(Collectors.toList());
  }
}
