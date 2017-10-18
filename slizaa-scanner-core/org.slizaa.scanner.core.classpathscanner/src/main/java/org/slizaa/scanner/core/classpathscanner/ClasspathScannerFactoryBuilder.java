package org.slizaa.scanner.core.classpathscanner;

import java.util.function.Function;

import org.slizaa.scanner.core.classpathscanner.internal.ClasspathScannerFactory;

/**
 */
public class ClasspathScannerFactoryBuilder {

  /** - */
  private ClasspathScannerFactory _classpathScannerFactory;

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public static final ClasspathScannerFactoryBuilder newClasspathScannerFactory() {
    return new ClasspathScannerFactoryBuilder();
  }

  public ClasspathScannerFactoryBuilder() {
    _classpathScannerFactory = new ClasspathScannerFactory();
  }

  public <T> ClasspathScannerFactoryBuilder registerCodeSourceClassLoaderProvider(Class<T> type,
      Function<T, ClassLoader> classLoaderProvider) {

    //
    _classpathScannerFactory.registerCodeSourceClassLoaderProvider(type, classLoaderProvider);

    //
    return this;
  }

  public IClasspathScannerFactory create() {
    return _classpathScannerFactory;
  }
}
