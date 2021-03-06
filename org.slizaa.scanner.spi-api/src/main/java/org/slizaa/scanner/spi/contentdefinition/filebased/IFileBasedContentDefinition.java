/*******************************************************************************
 * Copyright (C) 2011-2017 Gerd Wuetherich (gerd@gerd-wuetherich.de). All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Gerd Wuetherich (gerd@gerd-wuetherich.de) - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.spi.contentdefinition.filebased;

import java.io.File;
import java.util.Collection;
import java.util.Set;

import org.slizaa.scanner.spi.contentdefinition.ContentType;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinition;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IFileBasedContentDefinition extends IContentDefinition {

  /**
   * <p>
   * Returns a {@link Set} of all resources of the specified type
   * </p>
   * <p>
   * If this content entry is not a resource content ( <code>isAnalyze()</code> returns <code>false</code>), an empty
   * set will be returned.
   * </p>
   *
   * @param type
   * @return a Set of resources, never null.
   */
  Collection<IFile> getFiles(ContentType type);

  /**
   * <p>
   * Returns a {@link Set} of all binary resources. This is a convenience method for {@link #getFiles(ContentType)
   * getResources(ContentType.BINARY)}
   * </p>
   * <p>
   * If this content entry is not a resource content ( <code>isAnalyze()</code> returns <code>false</code>), an empty
   * set will be returned.
   * </p>
   *
   * @return a Set of resources, never null.
   */
  Collection<IFile> getBinaryFiles();

  /**
   * <p>
   * Returns all source resources. This is a convenience method for {@link #getFiles(ContentType)
   * getResources(ContentType.SOURCE)}
   * </p>
   * <p>
   * If this content entry is not a resource content ( <code>isAnalyze()</code> returns <code>false</code>), an empty
   * set will be returned.
   * </p>
   *
   * @return a Set of resources, never null.
   */
  Collection<IFile> getSourceFiles();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  Collection<File> getBinaryRootPaths();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  Collection<File> getSourceRootPaths();
}
