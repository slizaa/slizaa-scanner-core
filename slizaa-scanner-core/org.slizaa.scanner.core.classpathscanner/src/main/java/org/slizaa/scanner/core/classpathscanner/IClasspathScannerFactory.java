package org.slizaa.scanner.core.classpathscanner;

import java.util.List;

/**
 * <p>
 * </p>
 */
public interface IClasspathScannerFactory {

  /** - */
  public static final String SLIZAA_EXTENSION_BUNDLE_HEADER = "Slizaa-Extension";

  /**
   * <p>
   * </p>
   *
   * @param elementsToScan
   * @param processors
   */
  IClasspathScanner createScanner(List<?> elementsToScan);

  /**
   * <p>
   * </p>
   *
   * @param elementToScan
   * @param processors
   */
  IClasspathScanner createScanner(Object... elementsToScan);
}
