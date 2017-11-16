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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    classpathScanner.createScanner(pathToScan)
        .matchClassesWithAnnotation(TestAnnotation.class, (codeSource, classes) -> {

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
    Map<Object, List<String>> files = new HashMap<>();

    //
    classpathScanner.createScanner(pathToScan).matchFiles("cypher",

        // the item transformer
        (relativePath, inputStream, lengthBytes) -> {
          return extract(inputStream, lengthBytes);
        },

        // the collector
        (codeSource, paths) -> {
          files.put(codeSource, paths);
        }).scan();

    //
    Stopwatch stopwatch = Stopwatch.createStarted();

    //
    assertThat(files).hasSize(1);
    assertThat(files.get(pathToScan).get(0)).contains("@slizaa.name type-references");
    System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
  }

  /**
   * <p>
   * </p>
   *
   * @param inputStream
   * @param lengthBytes
   * @return
   */
  private String extract(InputStream inputStream, long lengthBytes) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int read = 0;
      while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
        baos.write(buffer, 0, read);
      }
      baos.flush();
      return new String(baos.toByteArray(), "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
