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
package org.slizaa.scanner.spi.contentdefinition.filebased;

import static org.slizaa.scanner.spi.internal.Preconditions.checkNotNull;

import java.io.File;

import org.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.spi.contentdefinition.ContentType;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import org.slizaa.scanner.spi.internal.contentdefinition.filebased.FileBasedContentDefinition;

public class FileBasedContentDefinitionFactory {

  /**
   * <p>
   * </p>
   * 
   * @param contentName
   * @param contentVersion
   * @param binaryPaths
   * @param sourcePaths
   * @param analyzeMode
   * @return
   */
  public static IContentDefinition createFileBasedContentDefinition(String contentName, String contentVersion,
      File[] binaryPaths, File[] sourcePaths, AnalyzeMode analyzeMode) {

    // asserts
    checkNotNull(contentName);
    checkNotNull(contentVersion);
    checkNotNull(binaryPaths);
    checkNotNull(analyzeMode);

    FileBasedContentDefinition result = new FileBasedContentDefinition();

    result.setAnalyzeMode(analyzeMode);
    result.setName(contentName);
    result.setVersion(contentVersion);

    for (File binaryPath : binaryPaths) {
      result.addRootPath(binaryPath, ContentType.BINARY);
    }

    if (sourcePaths != null) {
      for (File sourcePath : sourcePaths) {
        result.addRootPath(sourcePath, ContentType.SOURCE);
      }
    }

    // initialize the result
    result.initialize();

    //
    return result;
  }
}
