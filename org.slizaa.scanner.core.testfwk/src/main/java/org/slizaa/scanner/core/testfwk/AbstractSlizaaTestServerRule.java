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
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slizaa.core.classpathscanner.ClasspathScannerFactoryBuilder;
import org.slizaa.core.classpathscanner.IClasspathScanner;
import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverService.IMvnResolverJob;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.cypherregistry.CypherStatementRegistry;
import org.slizaa.scanner.core.cypherregistry.SlizaaCypherFileParser;
import org.slizaa.scanner.core.spi.parser.IParserFactory;

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
  private BackEndLoader             _backEndLoader;

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
   * {@inheritDoc}
   */
  @Override
  public Statement apply(Statement base, Description description) {

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {

        //
        AbstractSlizaaTestServerRule.this._backEndLoader = new BackEndLoader(
            AbstractSlizaaTestServerRule.this._backEndLoaderConfigurer);

        //
        AbstractSlizaaTestServerRule.this._graphDb = createGraphDatabase(
            AbstractSlizaaTestServerRule.this._backEndLoader);

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
   */
  protected abstract IGraphDb createGraphDatabase(BackEndLoader backEndLoader);

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
  public static class BackEndLoader {

    /** - */
    private IModelImporterFactory    _modelImporterFactory;

    /** - */
    private IGraphDbFactory          _graphDbFactory;

    /** - */
    private List<IParserFactory>     _parserFactories;

    /** - */
    private ICypherStatementRegistry _cypherStatementRegistry;

    /** - */
    private ClassLoader              _classLoader;

    /**
     * <p>
     * Creates a new instance of type {@link BackEndLoader}.
     * </p>
     *
     * @param configurer
     */
    public BackEndLoader(Consumer<IMvnResolverJob> configurer) {
      this(configurer, BackEndLoader.class.getClassLoader());
    }

    /**
     * <p>
     * Creates a new instance of type {@link BackEndLoader}.
     * </p>
     *
     * @param configurer
     * @param mainClassLoader
     */
    public BackEndLoader(Consumer<IMvnResolverJob> configurer, ClassLoader mainClassLoader) {

      //
      checkNotNull(configurer);
      checkNotNull(mainClassLoader);

      // create new maven resolver job...
      IMvnResolverService mvnResolverService = MvnResolverServiceFactoryFactory.createNewResolverServiceFactory()
          .newMvnResolverService().withDefaultRemoteRepository()
          // TODO: Make configurable!
          .withRemoteRepository("oss_sonatype_snapshots", "https://oss.sonatype.org/content/repositories/snapshots")
          .create();

      //
      IMvnResolverJob mvnResolverJob = mvnResolverService.newMvnResolverJob();

      // ...configure it...
      configurer.accept(mvnResolverJob);

      // ... and create a new class loader from the result
      this._classLoader = new URLClassLoader(mvnResolverJob.resolveToUrlArray(), mainClassLoader);

      // Step 1: load services via service loader mechanism
      this._modelImporterFactory = singleService(IModelImporterFactory.class, this._classLoader);
      this._graphDbFactory = singleService(IGraphDbFactory.class, this._classLoader);
      this._parserFactories = allServices(IParserFactory.class, this._classLoader);

      // Step 2: create the cypher statement registry
      IClasspathScanner urlclasspathScanner = ClasspathScannerFactoryBuilder.newClasspathScannerFactory()
          .registerCodeSourceClassLoaderProvider(URLClassLoader.class, cl1 -> cl1).create()
          .createScanner(this._classLoader, BackEndLoader.class.getClassLoader());

      //
      this._cypherStatementRegistry = new CypherStatementRegistry(() -> {
        List<ICypherStatement> result = new ArrayList<>();
        urlclasspathScanner
            .matchFiles("cypher", (name, stream, lengthBytes) -> SlizaaCypherFileParser.parse(name, stream),
                (codeSource1, items) -> result.addAll(items))
            .scan();
        return result;
      });
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public IModelImporterFactory getModelImporterFactory() {
      return this._modelImporterFactory;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public IGraphDbFactory getGraphDbFactory() {
      return this._graphDbFactory;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public ICypherStatementRegistry getCypherStatementRegistry() {
      return this._cypherStatementRegistry;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public List<IParserFactory> getParserFactories() {
      return this._parserFactories;
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public ClassLoader getClassLoader() {
      return this._classLoader;
    }

    /**
     * <p>
     * </p>
     *
     * @param type
     * @param classLoader
     * @return
     */
    private static <T> T singleService(Class<T> type, ClassLoader classLoader) {

      //
      checkNotNull(type);
      checkNotNull(classLoader);

      // get the service loader
      ServiceLoader<T> serviceLoader = ServiceLoader.load(type, classLoader);

      //
      Iterator<T> iterator = serviceLoader.iterator();
      if (!iterator.hasNext()) {
        throw new RuntimeException(String.format("No service of type '%s' available.", type));
      }

      //
      return iterator.next();
    }

    /**
     * <p>
     * </p>
     *
     * @param type
     * @param classLoader
     * @return
     */
    private static <T> List<T> allServices(Class<T> type, ClassLoader classLoader) {

      //
      checkNotNull(type);
      checkNotNull(classLoader);

      //
      List<T> result = new ArrayList<>();

      // get the service loader
      ServiceLoader<T> serviceLoader = ServiceLoader.load(type, classLoader);

      //
      Iterator<T> iterator = serviceLoader.iterator();
      while (iterator.hasNext()) {
        result.add(iterator.next());
      }

      //
      return result;
    }
  }

}
