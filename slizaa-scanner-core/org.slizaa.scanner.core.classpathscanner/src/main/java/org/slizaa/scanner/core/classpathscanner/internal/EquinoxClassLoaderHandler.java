/*
 * This file is part of FastClasspathScanner.
 * 
 * Author: Luke Hutchison
 * 
 * Hosted at: https://github.com/lukehutch/fast-classpath-scanner
 * 
 * --
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Luke Hutchison
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.slizaa.scanner.core.classpathscanner.internal;

import java.lang.reflect.Array;
import java.util.HashSet;

import io.github.lukehutch.fastclasspathscanner.classloaderhandler.ClassLoaderHandler;
import io.github.lukehutch.fastclasspathscanner.scanner.ClasspathFinder;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanSpec;
import io.github.lukehutch.fastclasspathscanner.utils.LogNode;
import io.github.lukehutch.fastclasspathscanner.utils.ReflectionUtils;

/** Extract classpath entries from the Eclipse Equinox ClassLoader. */
public class EquinoxClassLoaderHandler implements ClassLoaderHandler {

  public static final String[] HANDLED_CLASSLOADERS = { "org.eclipse.osgi.internal.loader.EquinoxClassLoader" };

  @Override
  public DelegationOrder getDelegationOrder(final ClassLoader classLoaderInstance) {
    return DelegationOrder.PARENT_FIRST;
  }

  private void addBundleFile(final Object bundlefile, final HashSet<Object> path, final ClassLoader classLoader,
      final ClasspathFinder classpathFinder, final LogNode log) throws Exception {
    if (bundlefile != null) {
      // Don't get stuck in infinite loop
      if (path.add(bundlefile)) {
        // type File
        final Object basefile = ReflectionUtils.getFieldVal(bundlefile, "basefile");
        if (basefile != null) {
          // type String
          final Object cp = ReflectionUtils.getFieldVal(bundlefile, "cp");
          if (cp != null) {
            // We found the base file and a classpath
            // element, e.g. "bin/"
            classpathFinder.addClasspathElement(basefile.toString() + "/" + cp.toString(), classLoader, log);
          } else {
            // No classpath element found, just use basefile
            classpathFinder.addClasspathElement(basefile.toString(), classLoader, log);
          }
        }
        addBundleFile(ReflectionUtils.getFieldVal(bundlefile, "wrapped"), path, classLoader, classpathFinder, log);
        addBundleFile(ReflectionUtils.getFieldVal(bundlefile, "next"), path, classLoader, classpathFinder, log);
      }
    }
  }

  @Override
  public void handle(final ClassLoader classLoader, final ClasspathFinder classpathFinder, final ScanSpec scanSpec,
      final LogNode log) throws Exception {
    // type ClasspathManager
    final Object manager = ReflectionUtils.getFieldVal(classLoader, "manager");
    // type ClasspathEntry[]
    final Object entries = ReflectionUtils.getFieldVal(manager, "entries");
    if (entries != null) {
      for (int i = 0, n = Array.getLength(entries); i < n; i++) {
        // type ClasspathEntry
        final Object entry = Array.get(entries, i);
        // type BundleFile
        final Object bundlefile = ReflectionUtils.getFieldVal(entry, "bundlefile");
        addBundleFile(bundlefile, new HashSet<>(), classLoader, classpathFinder, log);
      }
    }
  }
}
