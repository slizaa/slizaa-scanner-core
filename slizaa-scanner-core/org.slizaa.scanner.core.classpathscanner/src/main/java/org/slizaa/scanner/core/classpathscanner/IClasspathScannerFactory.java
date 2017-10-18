/*******************************************************************************
 * Copyright (C) 2011-2017 Gerd Wuetherich (gerd@gerd-wuetherich.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerd Wuetherich (gerd@gerd-wuetherich.de) - initial API and implementation
 ******************************************************************************/
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
