/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.core.testfwk;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import org.slizaa.core.mvnresolver.api.IMvnResolverService.IMvnResolverJob;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.api.importer.IModelImporter;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.parser.IParserFactory;
import org.slizaa.scanner.core.testfwk.internal.ZipUtil;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaTestServerRule extends AbstractSlizaaTestServerRule {

  /** - */
  private IContentDefinitionProvider _contentDefinitionProvider;

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param contentDefinitionProvider
   * @param backendLoaderConfigurer
   */
  public SlizaaTestServerRule(IContentDefinitionProvider contentDefinitionProvider,
      Consumer<IMvnResolverJob> backEndLoaderConfigurer) {

    //
    super(backEndLoaderConfigurer);

    //
    this._contentDefinitionProvider = contentDefinitionProvider;
  }

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param workingDirectory
   * @param contentDefinitions
   */
  public SlizaaTestServerRule(File workingDirectory, IContentDefinitionProvider contentDefinitionProvider,
      Consumer<IMvnResolverJob> backEndLoaderConfigurer) {

    //
    super(workingDirectory, backEndLoaderConfigurer);

    //
    this._contentDefinitionProvider = contentDefinitionProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IGraphDb createGraphDatabase(BackEndLoader backEndLoader) {

    //
    IGraphDbFactory graphDbFactory = backEndLoader.getGraphDbFactory();
    IModelImporterFactory modelImporterFactory = backEndLoader.getModelImporterFactory();
    List<IParserFactory> parserFactories = backEndLoader.getParserFactories();
    ICypherStatementRegistry cypherStatementRegistry = backEndLoader.getCypherStatementRegistry();

    // parse
    IModelImporter modelImporter = modelImporterFactory.createModelImporter(
        SlizaaTestServerRule.this._contentDefinitionProvider, SlizaaTestServerRule.this.getDatabaseDirectory(),
        parserFactories, cypherStatementRegistry.getAllStatements());

    //
    executeWithThreadContextClassLoader(backEndLoader.getClassLoader(),
        () -> modelImporter.parse(new ConsoleLogProgressMonitor(),
            () -> graphDbFactory.newGraphDb(5001, SlizaaTestServerRule.this.getDatabaseDirectory()).create()));

    //
    return modelImporter.getGraphDb();
  }

  /**
   * <p>
   * </p>
   *
   * @param file
   * @throws Exception
   */
  public void exportDatabaseAsZipFile(String file, boolean restart) throws Exception {
    this.getGraphDb().shutdown();

    ZipUtil.zipFile(this.getDatabaseDirectory().getAbsolutePath(), checkNotNull(file), true);
    if (restart) {
      throw new UnsupportedOperationException();
      // setGraphDb(this._backendLoader.getGraphDbFactory().newGraphDb(5001, this._databaseDirectory).create());
    }
  }
}
