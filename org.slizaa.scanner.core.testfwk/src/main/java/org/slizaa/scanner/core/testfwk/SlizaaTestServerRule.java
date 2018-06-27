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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slizaa.core.mvnresolver.api.IMvnResolverService.IMvnResolverJob;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.api.importer.IModelImporter;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.parser.IParserFactory;
import org.slizaa.scanner.core.testfwk.internal.ScannerBackendLoader;
import org.slizaa.scanner.core.testfwk.internal.SlizaaTestProgressMonitor;
import org.slizaa.scanner.core.testfwk.internal.ZipUtil;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaTestServerRule implements TestRule {

  /** - */
  private File                       _databaseDirectory;

  /** - */
  private IGraphDb                   _graphDb;

  /** - */
  private IContentDefinitionProvider _contentDefinitionsSupplier;

  /** - */
  private List<Class<?>>             _extensionClasses;

  /** - */
  private ScannerBackendLoader       _backendLoader;

  /** - */
  private Consumer<IMvnResolverJob>  _backendLoaderConfigurer;

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param contentDefinitions
   * @param backendLoaderConfigurer
   */
  public SlizaaTestServerRule(IContentDefinitionProvider contentDefinitions,
      Consumer<IMvnResolverJob> backendLoaderConfigurer) {
    this(createDatabaseDirectory(), contentDefinitions, backendLoaderConfigurer);
  }

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param workingDirectory
   * @param contentDefinitions
   */
  public SlizaaTestServerRule(File workingDirectory, IContentDefinitionProvider contentDefinitions,
      Consumer<IMvnResolverJob> backendLoaderConfigurer) {
    this._databaseDirectory = checkNotNull(workingDirectory);
    this._contentDefinitionsSupplier = checkNotNull(contentDefinitions);
    this._backendLoaderConfigurer = backendLoaderConfigurer;
    this._extensionClasses = new ArrayList<Class<?>>();
  }

  /**
   * <p>
   * </p>
   *
   * @param extensionClass
   * @return
   */
  public SlizaaTestServerRule withExtensionClass(Class<?> extensionClass) {
    this._extensionClasses.add(checkNotNull(extensionClass));
    return this;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public File getDatabaseDirectory() {
    return this._databaseDirectory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Statement apply(Statement base, Description description) {

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {

        //
        SlizaaTestServerRule.this._backendLoader = new ScannerBackendLoader(
            SlizaaTestServerRule.this._backendLoaderConfigurer);

        //
        IGraphDbFactory graphDbFactory = SlizaaTestServerRule.this._backendLoader.getGraphDbFactory();
        IModelImporterFactory modelImporterFactory = SlizaaTestServerRule.this._backendLoader.getModelImporterFactory();
        List<IParserFactory> parserFactories = SlizaaTestServerRule.this._backendLoader.getParserFactories();
        ICypherStatementRegistry cypherStatementRegistry = SlizaaTestServerRule.this._backendLoader
            .getCypherStatementRegistry();

        // parse
        IModelImporter modelImporter = modelImporterFactory.createModelImporter(
            SlizaaTestServerRule.this._contentDefinitionsSupplier, SlizaaTestServerRule.this._databaseDirectory,
            parserFactories, cypherStatementRegistry.getAllStatements());

        //
        executeWithThreadContextClassLoader(SlizaaTestServerRule.this._backendLoader.getClassLoader(),
            () -> modelImporter.parse(new SlizaaTestProgressMonitor(),
                () -> graphDbFactory.newGraphDb(5001, SlizaaTestServerRule.this._databaseDirectory).create()));

        //
        SlizaaTestServerRule.this._graphDb = modelImporter.getGraphDb();

        try {
          base.evaluate();
        } finally {
          try {
            SlizaaTestServerRule.this._graphDb.close();
            Files.walk(SlizaaTestServerRule.this._databaseDirectory.toPath()).map(Path::toFile)
                .sorted((o1, o2) -> -o1.compareTo(o2)).forEach(File::delete);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    };
  }

  /**
   * <p>
   * </p>
   *
   * @param file
   * @throws Exception
   */
  public void exportDatabaseAsZipFile(String file, boolean restart) throws Exception {
    this._graphDb.shutdown();

    ZipUtil.zipFile(this._databaseDirectory.getAbsolutePath(), checkNotNull(file), true);
    if (restart) {
      this._graphDb = this._backendLoader.getGraphDbFactory().newGraphDb(5001, this._databaseDirectory).create();
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param runnable
   * @param classLoader
   */
  private void executeWithThreadContextClassLoader(ClassLoader classLoader, Runnable runnable) {

    //
    checkNotNull(runnable);
    checkNotNull(classLoader);

    //
    ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

    try {
      Thread.currentThread().setContextClassLoader(classLoader);
      runnable.run();
    } finally {
      Thread.currentThread().setContextClassLoader(oldClassLoader);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static File createDatabaseDirectory() {
    try {
      return Files.createTempDirectory("slizaaTestDatabases").toFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
