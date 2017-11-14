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

import java.lang.annotation.Annotation;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IClasspathScanner {

  /**
   * <p>
   * </p>
   *
   * @param clazz
   * @param processor
   * @return
   */
  IClasspathScanner matchClassesWithAnnotation(Class<? extends Annotation> clazz,
      IClassAnnotationMatchHandler processor);

  /**
   * <p>
   * </p>
   *
   * @param clazz
   * @param processor
   * @return
   */
  IClasspathScanner matchClassesWithMethodAnnotation(Class<? extends Annotation> clazz,
      IMethodAnnotationMatchHandler processor);

  /**
   * <p>
   * </p>
   *
   * @param extensionToMatch
   * @param processor
   * @return
   */
  IClasspathScanner matchFiles(String extensionToMatch, IFilenameMatchHandler processor);

  /**
   * <p>
   * </p>
   *
   */
  void scan();
}
