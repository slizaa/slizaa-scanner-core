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
package org.slizaa.scanner.core.impl.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerFactory;
import org.slizaa.scanner.core.classpathscanner.internal.ClasspathScannerFactory;

import com.google.common.base.Stopwatch;

public class ClasspathScannerTest {

  /**
   * <p>
   * </p>
   * 
   * @throws ClassNotFoundException
   */
  @Test
  public void testClasspathScanner_1() throws ClassNotFoundException {

    // create test class loader
    ClassLoader pathToScan = new URLClassLoader(
        new URL[] { this.getClass().getProtectionDomain().getCodeSource().getLocation() });

    //
    IClasspathScannerFactory classpathScanner = new ClasspathScannerFactory()
        .registerCodeSourceClassLoaderProvider(ClassLoader.class, cl -> cl);

    //
    List<Object> parserFactories = new ArrayList<>();

    //
    classpathScanner.createScanner(pathToScan).matchClassesWithAnnotation(TestAnnotation.class, (codeSource, classes) -> {

      //
      for (Class<?> clazz : classes) {
        try {
          parserFactories.add(clazz.newInstance());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    }).scan();

    //
    Stopwatch stopwatch = Stopwatch.createStarted();

    //
    assertThat(parserFactories).hasSize(1);
    System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
  }
  
  @Test
  public void testClasspathScanner_2() throws ClassNotFoundException {

    // create test class loader
    ClassLoader pathToScan = new URLClassLoader(
        new URL[] { this.getClass().getProtectionDomain().getCodeSource().getLocation() });

    //
    IClasspathScannerFactory classpathScanner = new ClasspathScannerFactory()
        .registerCodeSourceClassLoaderProvider(ClassLoader.class, cl -> cl);

    //
    List<String> files = new ArrayList<>();

    //
    classpathScanner.createScanner(pathToScan).matchFiles("cypher", (codeSource, paths) -> {

      //
      for (String path : paths) {
        try {
          files.add(path);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    }).scan();

    //
    Stopwatch stopwatch = Stopwatch.createStarted();

    //
    assertThat(files).hasSize(1);
    assertThat(files.get(0)).isEqualTo("test.cypher");
    System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
  }
}
