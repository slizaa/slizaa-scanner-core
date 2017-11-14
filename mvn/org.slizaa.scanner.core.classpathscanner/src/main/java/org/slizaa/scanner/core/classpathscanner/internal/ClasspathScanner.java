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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slizaa.scanner.core.classpathscanner.IClassAnnotationMatchHandler;
import org.slizaa.scanner.core.classpathscanner.IClasspathScanner;
import org.slizaa.scanner.core.classpathscanner.IFilenameMatchHandler;
import org.slizaa.scanner.core.classpathscanner.IMethodAnnotationMatchHandler;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.FileMatchProcessor;

public class ClasspathScanner implements IClasspathScanner {

  /** - */
  private ClasspathScannerFactory                                                       _factory;

  /** - */
  private List<?>                                                                       _elementsToScan;

  /** - */
  private Map<Class<? extends Annotation>, List<ClassAnnotationMatchProcessorAdapter>>  _classAnnotationMatchProcessors;

  /** - */
  private Map<Class<? extends Annotation>, List<MethodAnnotationMatchProcessorAdaptor>> _methodAnnotationMatchProcessors;

  /** - */
  private Map<String, List<FilenameMatchProcessorAdaptor>>                              _filenameMatchProcessors;

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
    _methodAnnotationMatchProcessors = new HashMap<>();
    _filenameMatchProcessors = new HashMap<>();
  }

  @Override
  public IClasspathScanner matchFiles(String extensionToMatch, IFilenameMatchHandler processor) {

    //
    FilenameMatchProcessorAdaptor adapter = new FilenameMatchProcessorAdaptor(processor);

    //
    List<FilenameMatchProcessorAdaptor> adapterList = _filenameMatchProcessors.computeIfAbsent(extensionToMatch,
        key -> new ArrayList<>());

    if (!adapterList.contains(adapter)) {

      // add...
      adapterList.add(adapter);
    }

    return this;
  }

  @Override
  public IClasspathScanner matchClassesWithAnnotation(Class<? extends Annotation> clazz,
      IClassAnnotationMatchHandler processor) {

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
  public IClasspathScanner matchClassesWithMethodAnnotation(Class<? extends Annotation> clazz,
      IMethodAnnotationMatchHandler processor) {

    //
    MethodAnnotationMatchProcessorAdaptor adapter = new MethodAnnotationMatchProcessorAdaptor(processor);

    //
    List<MethodAnnotationMatchProcessorAdaptor> adapterList = _methodAnnotationMatchProcessors.computeIfAbsent(clazz,
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
        processor.beforeScan(codeSource);
        classpathScanner.matchClassesWithAnnotation(entry.getKey(), classWithAnnotation -> {
          processor.addClassWithAnnotation(classWithAnnotation);
        });
      });
    });

    //
    _methodAnnotationMatchProcessors.entrySet().forEach(entry -> {
      entry.getValue().forEach(processor -> {
        processor.beforeScan(codeSource);
        classpathScanner.matchClassesWithMethodAnnotation(entry.getKey(), (classWithAnnotation, method) -> {
          processor.addClassesWithMethodAnnotation(classWithAnnotation);
        });
      });
    });

    //
    _filenameMatchProcessors.entrySet().forEach(entry -> {
      entry.getValue().forEach(processor -> {
        processor.beforeScan(codeSource);
        classpathScanner.matchFilenameExtension(entry.getKey(),
            (FileMatchProcessor) (relativePath, inputStream, lengthBytes) -> {
              processor.addPath(relativePath);
            });
      });
    });

    // Actually perform the scan (nothing will happen without this call)
    classpathScanner.scan();

    // call after scan
    _classAnnotationMatchProcessors.entrySet().forEach(entry -> {
      entry.getValue().forEach(processor -> {
        processor.afterScan(codeSource);
      });
    });

    // call after scan
    _methodAnnotationMatchProcessors.entrySet().forEach(entry -> {
      entry.getValue().forEach(processor -> {
        processor.afterScan(codeSource);
      });
    });
    
    // call after scan
    _filenameMatchProcessors.entrySet().forEach(entry -> {
      entry.getValue().forEach(processor -> {
        processor.afterScan(codeSource);
      });
    });
  }
}
