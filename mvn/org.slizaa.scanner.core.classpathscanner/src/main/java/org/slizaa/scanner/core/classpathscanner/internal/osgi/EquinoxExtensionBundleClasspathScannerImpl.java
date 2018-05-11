package org.slizaa.scanner.core.classpathscanner.internal.osgi;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class EquinoxExtensionBundleClasspathScannerImpl implements IClasspathScannerService {

  /** - */
  private ExtensionBundleTracker _extensionBundleTracker;

  /**
   * <p>
   * </p>
   *
   * @param context
   */
  public void actviate(BundleContext context) {

    //
    this._extensionBundleTracker = new ExtensionBundleTracker(context);
    this._extensionBundleTracker.open();
  }

  /**
   * <p>
   * </p>
   */
  public void deactviate() {

    //
    this._extensionBundleTracker.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Class<?>> getExtensionsWithClassAnnotation(Class<? extends Annotation> annotationType) {

    //
    List<Class<?>> result = new ArrayList<>();

    //
    for (Entry<Bundle, ExtensionHolder> entry : this._extensionBundleTracker.getTracked().entrySet()) {
      result.addAll(entry.getValue().getExtensionsWithClassAnnotation(annotationType));
    }

    //
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> List<Class<T>> getExtensionsWithClassAnnotation(Class<? extends Annotation> annotationType,
      Class<T> targetType) {

    //
    checkNotNull(targetType);

    //
    List<Class<T>> result = new ArrayList<>();

    //
    for (Entry<Bundle, ExtensionHolder> entry : this._extensionBundleTracker.getTracked().entrySet()) {

      //
      @SuppressWarnings("unchecked")
      List<Class<T>> partialResult = entry.getValue().getExtensionsWithClassAnnotation(annotationType).stream()
          .filter(cl -> targetType.isAssignableFrom(cl)).map(cl -> (Class<T>) cl).collect(Collectors.toList());

      //
      result.addAll(partialResult);
    }

    //
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Class<?>> getExtensionsWithMethodAnnotation(Class<? extends Annotation> annotationType) {

    //
    List<Class<?>> result = new ArrayList<>();

    //
    for (Entry<Bundle, ExtensionHolder> entry : this._extensionBundleTracker.getTracked().entrySet()) {
      result.addAll(entry.getValue().getExtensionsWithMethodAnnotation(annotationType));
    }

    //
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> List<Class<T>> getExtensionsWithMethodAnnotation(Class<? extends Annotation> annotationType,
      Class<T> targetType) {

    //
    checkNotNull(targetType);

    //
    List<Class<T>> result = new ArrayList<>();

    //
    for (Entry<Bundle, ExtensionHolder> entry : this._extensionBundleTracker.getTracked().entrySet()) {

      //
      @SuppressWarnings("unchecked")
      List<Class<T>> partialResult = entry.getValue().getExtensionsWithMethodAnnotation(annotationType).stream()
          .filter(cl -> targetType.isAssignableFrom(cl)).map(cl -> (Class<T>) cl).collect(Collectors.toList());

      //
      result.addAll(partialResult);
    }

    //
    return result;
  }

  @Override
  public <T> List<T> getFiles(String postfix, Class<T> resultType, BiFunction<String, InputStream, T> transformer) {

    //
    checkNotNull(postfix);
    checkNotNull(resultType);
    checkNotNull(transformer);

    //
    List<T> result = new ArrayList<>();

    //
    for (Entry<Bundle, ExtensionHolder> entry : this._extensionBundleTracker.getTracked().entrySet()) {
      result.addAll(entry.getValue().getFiles(postfix, resultType, transformer));
    }

    //
    return result;
  }
}
