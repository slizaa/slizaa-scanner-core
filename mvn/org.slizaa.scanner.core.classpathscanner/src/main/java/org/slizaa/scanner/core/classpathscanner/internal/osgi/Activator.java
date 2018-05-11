/**
 *
 */
package org.slizaa.scanner.core.classpathscanner.internal.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class Activator implements BundleActivator {

  private EquinoxExtensionBundleClasspathScannerImpl _classpathScannerService;

  @Override
  public void start(BundleContext context) throws Exception {
    this._classpathScannerService = new EquinoxExtensionBundleClasspathScannerImpl();
    this._classpathScannerService.actviate(context);
    context.registerService(IClasspathScannerService.class, this._classpathScannerService, null);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    this._classpathScannerService.deactviate();
    this._classpathScannerService = null;
  }
}
