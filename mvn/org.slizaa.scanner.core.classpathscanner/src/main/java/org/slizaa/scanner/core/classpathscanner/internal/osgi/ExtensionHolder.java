package org.slizaa.scanner.core.classpathscanner.internal.osgi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.HashMap;
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
    this._bundle = checkNotNull(bundle);
    this._classpathScannerFactory = checkNotNull(classpathScannerFactory);
    this._extensionsWithClassAnnotation = new HashMap<>();
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
            System.out.println("EXT: " + exts);
            this._extensionsWithClassAnnotation.put(annotationType, exts);
          })

          //
          .scan();
    }

    //
    return this._extensionsWithClassAnnotation.get(annotationType);
  }
}
