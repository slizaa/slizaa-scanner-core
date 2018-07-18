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
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.spi.parser.IParserFactory;
import org.slizaa.scanner.core.testfwk.internal.BackEndLoader;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractSlizaaTestServerRule implements TestRule {

  /** - */
  private File                      _databaseDirectory;

  /** - */
  private IGraphDb                  _graphDb;

  /** - */
  private List<Class<?>>            _extensionClasses;

  /** - */
  private ITestFwkBackEnd           _testFwkBackEnd;

  /** - */
  private Consumer<IMvnResolverJob> _backEndLoaderConfigurer;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractSlizaaTestServerRule}.
   * </p>
   *
   * @param backendLoaderConfigurer
   */
  public AbstractSlizaaTestServerRule(Consumer<IMvnResolverJob> backendLoaderConfigurer) {
    this(createDatabaseDirectory(), backendLoaderConfigurer);
  }

  /**
   * <p>
   * Creates a new instance of type {@link AbstractSlizaaTestServerRule}.
   * </p>
   *
   * @param workingDirectory
   * @param backendLoaderConfigurer
   */
  public AbstractSlizaaTestServerRule(File workingDirectory, Consumer<IMvnResolverJob> backendLoaderConfigurer) {
    this._databaseDirectory = checkNotNull(workingDirectory);
    this._backEndLoaderConfigurer = backendLoaderConfigurer;
    this._extensionClasses = new ArrayList<Class<?>>();
  }

  /**
   * <p>
   * </p>
   *
   * @param extensionClass
   * @return
   */
  public AbstractSlizaaTestServerRule withExtensionClass(Class<?> extensionClass) {
    this._extensionClasses.add(checkNotNull(extensionClass));
    return this;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public IGraphDb getGraphDb() {
    return this._graphDb;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<Class<?>> getExtensionClasses() {
    return this._extensionClasses;
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
   * <p>
   * </p>
   *
   * @return
   */
  public ITestFwkBackEnd getTestFwkBackEnd() {
    return _testFwkBackEnd;
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
        AbstractSlizaaTestServerRule.this._testFwkBackEnd = new BackEndLoader(
            AbstractSlizaaTestServerRule.this._backEndLoaderConfigurer);

        //
        AbstractSlizaaTestServerRule.this._graphDb = createGraphDatabase(
            AbstractSlizaaTestServerRule.this._testFwkBackEnd);

        try {
          base.evaluate();
        } finally {
          try {
            AbstractSlizaaTestServerRule.this._graphDb.close();
            Files.walk(AbstractSlizaaTestServerRule.this._databaseDirectory.toPath()).map(Path::toFile)
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
   * @param _backEndLoader2
   * @return
   * @throws IOException 
   */
  protected abstract IGraphDb createGraphDatabase(ITestFwkBackEnd testFwkBackEnd) throws IOException;

  /**
   * <p>
   * </p>
   *
   * @param graphDb
   */
  protected void setGraphDb(IGraphDb graphDb) {
    this._graphDb = graphDb;
  }

  /**
   * <p>
   * </p>
   *
   * @param runnable
   * @param classLoader
   */
  protected void executeWithThreadContextClassLoader(ClassLoader classLoader, Runnable runnable) {

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

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public interface ITestFwkBackEnd {

    /**
     * <p>
     * </p>
     *
     * @return
     */
    ClassLoader getClassLoader();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    List<IParserFactory> getParserFactories();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    ICypherStatementRegistry getCypherStatementRegistry();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    IGraphDbFactory getGraphDbFactory();

    /**
     * <p>
     * </p>
     *
     * @return
     */
    IModelImporterFactory getModelImporterFactory();
  }
}
