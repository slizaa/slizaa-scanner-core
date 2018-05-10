/**
 *
 */
package org.slizaa.scanner.core.cypherregistry.impl;

import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerService;
import org.slizaa.scanner.core.cypherregistry.CypherStatementRegistry;
import org.slizaa.scanner.core.cypherregistry.SlizaaCypherFileParser;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Activator implements BundleActivator {

  /** - */
  private ServiceTracker<IClasspathScannerService, IClasspathScannerService> _classpathScannerServiceTracker;

  /**
   * {@inheritDoc}
   */
  @Override
  public void start(BundleContext context) throws Exception {

    //
    this._classpathScannerServiceTracker = new ServiceTracker<>(context, IClasspathScannerService.class, null);
    this._classpathScannerServiceTracker.open();

    //
    ICypherStatementRegistry cypherStatementRegistry = new CypherStatementRegistry(() -> {

      // get the classpath scanner service...
      IClasspathScannerService classpathScannerService = this._classpathScannerServiceTracker.getService();

      //
      List<ICypherStatement> result = classpathScannerService != null
          ? classpathScannerService.getFiles("cypher", ICypherStatement.class,
              (name, stream) -> SlizaaCypherFileParser.parse(name, stream))
          : Collections.emptyList();

      return result;
    });

    context.registerService(ICypherStatementRegistry.class, cypherStatementRegistry, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop(BundleContext context) throws Exception {

    //
    this._classpathScannerServiceTracker.close();
    this._classpathScannerServiceTracker = null;
  }
}
