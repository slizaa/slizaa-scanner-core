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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slizaa.scanner.core.classpathscanner.IClassAnnotationMatchHandler;
import org.slizaa.scanner.core.classpathscanner.IClasspathScanner;
import org.slizaa.scanner.core.classpathscanner.IMethodAnnotationMatchHandler;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class ClasspathScanner implements IClasspathScanner {

  /** - */
  private ClasspathScannerFactory                                   _factory;

  /** - */
  private List<?>                                                   _elementsToScan;

  /** - */
  private Map<Class<?>, List<ClassAnnotationMatchProcessorAdapter>> _classAnnotationMatchProcessors;

  /** - */
  private List<IMethodAnnotationMatchHandler>                     _methodAnnotationMatchHandlers;

  /**
   * <p>
   * Creates a new instance of type {@link ClasspathScanner}.
   * </p>
   *
   * @param factory
   */
  public ClasspathScanner(ClasspathScannerFactory factory, List<?> elementsToScan) {
    _factory = checkNotNull(factory);
    _elementsToScan = checkNotNull(elementsToScan);

    _classAnnotationMatchProcessors = new HashMap<>();
    _methodAnnotationMatchHandlers = new ArrayList<>();
  }

  @Override
  public IClasspathScanner matchClassesWithAnnotation(Class<?> clazz, IClassAnnotationMatchHandler processor) {

    //
    ClassAnnotationMatchProcessorAdapter adapter = new ClassAnnotationMatchProcessorAdapter(processor);

    //
    List<ClassAnnotationMatchProcessorAdapter> adapterList = _classAnnotationMatchProcessors.computeIfAbsent(clazz,
        key -> new ArrayList<>());

    if (!adapterList.contains(adapter)) {

      // add...
      adapterList.add(adapter);
    }

    return this;
  }

  @Override
  public void scan() {
    _elementsToScan.forEach(codeSource -> scanSingleElement(codeSource));

  }

  /**
   * <p>
   * </p>
   *
   * @param classLoaders
   */
  private void scanSingleElement(Object codeSource) {

    //
    ClassLoader classLoader = _factory.classLoader(codeSource);

    //
    FastClasspathScanner classpathScanner = new FastClasspathScanner()

        // ignore parent class loaders
        .ignoreParentClassLoaders(true)

        //
        .registerClassLoaderHandler(EquinoxClassLoaderHandler.class)

        // set the class loader to scan
        .overrideClassLoaders(classLoader);

    //
    _classAnnotationMatchProcessors.entrySet().forEach(entry -> {

      entry.getValue().forEach(processor -> {

        // call before scan
        processor.beforeScan(codeSource);

        // add handler to class path scanner
        classpathScanner.matchClassesWithAnnotation(entry.getKey(), classWithAnnotation -> {
          processor.addClassWithAnnotation(classWithAnnotation);
        });
      });
    });

    // Actually perform the scan (nothing will happen without this call)
    classpathScanner.scan();

    //
    _classAnnotationMatchProcessors.entrySet().forEach(entry -> {

      entry.getValue().forEach(processor -> {

        // call after scan
        processor.afterScan(codeSource);
      });
    });
  }
}
