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
package org.slizaa.scanner.api.importer;

import org.slizaa.scanner.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.spi.parser.IParserFactory;

import java.io.File;
import java.util.List;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IModelImporterFactory {

  /**
   * <p>
   * Creates a new {@link IModelImporter} for the specified {@link IContentDefinitionProvider}.
   * </p>
   * 
   * @param contentDefinitionProvider
   *          the content definition of the system that should be analyzed
   * @param databaseDirectory
   *          the directory where the neo4j database should be stored
   * @param parserFactories
   *          the parser factories that provide the parsers that should be used while analyzing the defined system
   * @return a new {@link IModelImporter} instance
   */
  IModelImporter createModelImporter(IContentDefinitionProvider contentDefinitionProvider, File databaseDirectory,
      List<IParserFactory> parserFactories, List<ICypherStatement> cypherStatements);
}
