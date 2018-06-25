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
package org.slizaa.scanner.core.spi.contentdefinition;

import static org.slizaa.scanner.core.spi.internal.Preconditions.checkNotNull;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slizaa.scanner.core.spi.internal.contentdefinition.filebased.FileBasedContentDefinition;

/**
 * <p>
 * Superclass for all implementations of {@link ITempDefinitionProvider}
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractContentDefinitionProvider implements IContentDefinitionProvider {

  /** the list of content definitions */
  private List<IContentDefinition> _contentDefinitions;

  /** indicates whether or not this provider has been initialized */
  private boolean                  _isInitialized;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractContentDefinitionProvider}.
   * </p>
   * 
   * @param contentDefinitionProvider
   */
  public AbstractContentDefinitionProvider() {

    //
    _contentDefinitions = new LinkedList<IContentDefinition>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<IContentDefinition> getContentDefinitions() {

    //
    initialize();

    //
    return Collections.unmodifiableList(_contentDefinitions);
  }

  /**
   * <p>
   * </p>
   */
  protected abstract void onInitializeProjectContent();

  /**
   * <p>
   * </p>
   */
  protected abstract void onDisposeProjectContent();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected List<IContentDefinition> contentDefinitions() {
    return _contentDefinitions;
  }

  /**
   * <p>
   * </p>
   * 
   * @param progressMonitor
   * @return
   */
  protected final void initialize() {

    if (!_isInitialized) {

      onInitializeProjectContent();

      _isInitialized = true;
    }
  }

  /**
   * <p>
   * </p>
   */
  protected void dispose() {

    if (_isInitialized) {

      onDisposeProjectContent();

      _isInitialized = false;

      _contentDefinitions.clear();
    }
  }

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
  protected IContentDefinition createFileBasedContentDefinition(String contentName, String contentVersion,
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
    _contentDefinitions.add(result);

    //
    return result;
  }
}
