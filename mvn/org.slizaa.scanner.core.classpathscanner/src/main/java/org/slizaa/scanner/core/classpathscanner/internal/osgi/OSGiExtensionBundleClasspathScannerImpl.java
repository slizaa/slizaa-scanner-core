package org.slizaa.scanner.core.classpathscanner.internal.osgi;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slizaa.scanner.core.classpathscanner.IOSGiBundleClasspathScannerService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component(immediate = true)
public class OSGiExtensionBundleClasspathScannerImpl implements IOSGiBundleClasspathScannerService {

  /** - */
  private ExtensionBundleTracker _extensionBundleTracker;

  /**
   * <p>
   * </p>
   *
   * @param context
   */
  @Activate
  public void actviate(BundleContext context) {

    //
    this._extensionBundleTracker = new ExtensionBundleTracker(context);
    this._extensionBundleTracker.open();
  }

  /**
   * <p>
   * </p>
   */
  @Deactivate
  public void deactviate() {

    //
    this._extensionBundleTracker.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Object> getExtensionsWithClassAnnotation(Class<? extends Annotation> annotationType) {

    //
    List<Object> result = new ArrayList<Object>();

    //
    for (Entry<Bundle, ExtensionHolder> entry : this._extensionBundleTracker.getTracked().entrySet()) {

      //
      System.out.println("TRACKED: " + entry.getKey().getSymbolicName());

      //
      result.addAll(entry.getValue().getExtensionsWithClassAnnotation(annotationType));
    }

    //
    return result;
  }

}
