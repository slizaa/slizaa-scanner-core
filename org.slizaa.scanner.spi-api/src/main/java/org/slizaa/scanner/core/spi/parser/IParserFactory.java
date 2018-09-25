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
package org.slizaa.scanner.spi.parser;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

/**
 * <p>
 * A parser factory is responsible for creating project parser.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IParserFactory {

  /**
   * <p>
   * This method is called immediately after a {@link IParserFactory} has been created.
   * </p>
   */
  void initialize();

  /**
   * <p>
   * This method is called before a {@link IParserFactory} will be destroyed.
   * </p>
   */
  void dispose();

  /**
   * <p>
   * Creates a new instance of type {@link IParser}.
   * </p>
   * 
   * @return the newly created {@link IParser}
   */
  IParser createParser(IContentDefinitionProvider systemDefinition);

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param cypherStatementExecutor
   * @param subMonitor
   * @throws Exception
   */
  void batchParseStart(IContentDefinitionProvider systemDefinition, ICypherStatementExecutor cypherStatementExecutor, IProgressMonitor subMonitor)
      throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param cypherStatementExecutor
   * @param subMonitor
   * @throws Exception
   */
  void batchParseStop(IContentDefinitionProvider systemDefinition, ICypherStatementExecutor cypherStatementExecutor, IProgressMonitor subMonitor)
      throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   * @throws Exception
   */
  void batchParseStartContentDefinition(IContentDefinition contentDefinition) throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   * @throws Exception
   */
  void batchParseStopContentDefinition(IContentDefinition contentDefinition) throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  static abstract class Adapter implements IParserFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStart(IContentDefinitionProvider systemDefinition, ICypherStatementExecutor cypherStatementExecutor,
        IProgressMonitor subMonitor) throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStop(IContentDefinitionProvider systemDefinition, ICypherStatementExecutor cypherStatementExecutor,
        IProgressMonitor subMonitor) throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStartContentDefinition(IContentDefinition contentDefinition) throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStopContentDefinition(IContentDefinition contentDefinition) throws Exception {
      // empty default implementation
    }
  }
}
