package org.slizaa.scanner.core.classpathscanner.internal.osgi;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.BundleTracker;
import org.slizaa.scanner.core.classpathscanner.ClasspathScannerFactoryBuilder;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ExtensionBundleTracker extends BundleTracker<ExtensionHolder> {

  /** - */
  private IClasspathScannerFactory _classpathScannerFactory;

  /**
   * <p>
   * Creates a new instance of type {@link ExtensionBundleTracker}.
   * </p>
   *
   * @param context
   */
  public ExtensionBundleTracker(BundleContext context) {
    super(context, Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE, null);

    //
    this._classpathScannerFactory = ClasspathScannerFactoryBuilder.newClasspathScannerFactory()

        //
        .registerCodeSourceClassLoaderProvider(Bundle.class, (b) -> {
          return b.adapt(BundleWiring.class).getClassLoader();
        })

        //
        .create();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExtensionHolder addingBundle(Bundle bundle, BundleEvent event) {

    //
    String slizaaExtension = bundle.getHeaders().get(IClasspathScannerFactory.SLIZAA_EXTENSION_BUNDLE_HEADER);

    // create the result
    return slizaaExtension != null && !slizaaExtension.isEmpty() ? new ExtensionHolder(bundle, _classpathScannerFactory) : null;
  }
}
