/*******************************************************************************
 * Copyright (C) 2011-2017 Gerd Wuetherich (gerd@gerd-wuetherich.de). All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Gerd Wuetherich (gerd@gerd-wuetherich.de) - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.api.importer;

import org.slizaa.core.progressmonitor.IProgressMonitor;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.spi.parser.IProblem;

import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 * {@link IModelImporter IModelImporters} can be used to parse systems (defined by {@link IContentDefinitionProvider
 * IContentDefinitionProviders}).
 * </p>
 * <p>
 * To create {@link IModelImporter} instances you have to use the {@link IModelImporterFactory}: <code><pre>
 * // get the factory
 * IModelImporterFactory modelImporterFactory = ...;
 *
 * // create a new IModelImporter instance
 * modelImporterFactory.createModelImporter(systemDefinition, databaseDirectory, parserFactories);
 * </pre></code>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IModelImporter {

  /**
   * <p>
   * Parses the underlying {@link IContentDefinitionProvider}.
   * </p>
   *
   * @param monitor
   *          the progress monitor
   * @return the list with all problems that may occurred while parsing the system
   */
  List<IProblem> parse(IProgressMonitor monitor);

  /**
   * <p>
   * </p>
   *
   * @param monitor
   * @param graphDbSupplier
   * @return
   */
  List<IProblem> parse(IProgressMonitor monitor, Supplier<IGraphDb> graphDbSupplier);

  /**
   * <p>
   * </p>
   *
   * @return
   */
  IGraphDb getGraphDb();
}
