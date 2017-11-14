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
package org.slizaa.scanner.core.classpathscanner.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import org.slizaa.scanner.core.classpathscanner.IClassAnnotationMatchHandler;

public class ClassAnnotationMatchProcessorAdapter {

  /** - */
  private IClassAnnotationMatchHandler _processor;

  /** - */
  private List<Class<?>>                 _result;

  /**
   * <p>
   * Creates a new instance of type {@link ClassAnnotationMatchProcessorAdapter}.
   * </p>
   *
   * @param processor
   */
  public ClassAnnotationMatchProcessorAdapter(IClassAnnotationMatchHandler processor) {

    //
    checkNotNull(processor);

    //
    _processor = processor;
  }

  /**
   * <p>
   * </p>
   * @param codeSource 
   *
   */
  public void beforeScan(Object codeSource) {
    _result = new LinkedList<>();
  }

  /**
   * <p>
   * </p>
   *
   * @param classWithAnnotation
   */
  public void addClassWithAnnotation(Class<?> classWithAnnotation) {
    _result.add(classWithAnnotation);
  }

  /**
   * <p>
   * </p>
   *
   * @param codeSource
   * @param classLoader
   */
  public void afterScan(Object codeSource) {
    _processor.processMatch(codeSource, _result);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_processor == null) ? 0 : _processor.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClassAnnotationMatchProcessorAdapter other = (ClassAnnotationMatchProcessorAdapter) obj;
    if (_processor == null) {
      if (other._processor != null)
        return false;
    } else if (!_processor.equals(other._processor))
      return false;
    return true;
  }
}
