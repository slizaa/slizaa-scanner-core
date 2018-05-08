package org.slizaa.scanner.core.classpathscanner.internal.osgi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

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
    _bundle = checkNotNull(bundle);
    _classpathScannerFactory = checkNotNull(classpathScannerFactory);
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
    if (!_extensionsWithClassAnnotation.containsKey(checkNotNull(annotationType))) {

      // scan the bundle
      this._classpathScannerFactory

          //
          .createScanner(_bundle)

          //
          .matchClassesWithAnnotation(annotationType,
              (b, exts) -> _extensionsWithClassAnnotation.put(annotationType, exts))

          //
          .scan();
    }

    //
    return _extensionsWithClassAnnotation.get(annotationType);
  }
}
