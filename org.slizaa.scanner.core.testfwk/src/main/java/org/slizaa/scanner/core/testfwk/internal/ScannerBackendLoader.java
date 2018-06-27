/**
 *
 */
package org.slizaa.scanner.core.testfwk.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import org.slizaa.core.classpathscanner.ClasspathScannerFactoryBuilder;
import org.slizaa.core.classpathscanner.IClasspathScanner;
import org.slizaa.core.classpathscanner.IClasspathScannerFactory;
import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverService.IMvnResolverJob;
import org.slizaa.core.mvnresolver.api.IMvnResolverServiceFactory;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatementRegistry;
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
public class ScannerBackendLoader {

  /** - */
  private Consumer<IMvnResolverJob> _configurer;

  /** - */
  private IModelImporterFactory     _modelImporterFactory;

  /** - */
  private IGraphDbFactory           _graphDbFactory;

  /** - */
  private List<IParserFactory>      _parserFactories;

  /** - */
  private ICypherStatementRegistry  _cypherStatementRegistry;

  /** - */
  private ClassLoader               _classLoader;

  /**
   * <p>
   * Creates a new instance of type {@link ScannerBackendLoader}.
   * </p>
   *
   * @param configurer
   */
  public ScannerBackendLoader(Consumer<IMvnResolverJob> configurer) {
    this(configurer, ScannerBackendLoader.class.getClassLoader());
  }

  /**
   * <p>
   * Creates a new instance of type {@link ScannerBackendLoader}.
   * </p>
   *
   * @param configurer
   * @param mainClassLoader
   */
  public ScannerBackendLoader(Consumer<IMvnResolverJob> configurer, ClassLoader mainClassLoader) {

    //
    this._configurer = checkNotNull(configurer);

    //
    resolve(mainClassLoader);
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

  public IGraphDbFactory getGraphDbFactory() {
    return this._graphDbFactory;
  }

  public ICypherStatementRegistry getCypherStatementRegistry() {
    return this._cypherStatementRegistry;
  }

  public List<IParserFactory> getParserFactories() {
    return this._parserFactories;
  }

  public ClassLoader getClassLoader() {
    return this._classLoader;
  }

  /**
   * <p>
   * </p>
   *
   * @param
   */
  private void resolve(ClassLoader mainClassLoader) {

    //
    IMvnResolverServiceFactory resolverServiceFactory = MvnResolverServiceFactoryFactory
        .createNewResolverServiceFactory();

    //
    IMvnResolverService mvnResolverService = resolverServiceFactory.newMvnResolverService().create();

    //
    IMvnResolverJob mvnResolverJob = mvnResolverService.newMvnResolverJob();
    this._configurer.accept(mvnResolverJob);
    URL[] resolvedArtifacts = mvnResolverJob.resolveToUrlArray();

    //
    this._classLoader = new URLClassLoader(resolvedArtifacts, ScannerBackendLoader.class.getClassLoader());

    //
    this._modelImporterFactory = singleService(IModelImporterFactory.class, this._classLoader);
    this._graphDbFactory = singleService(IGraphDbFactory.class, this._classLoader);
    this._parserFactories = allServices(IParserFactory.class, this._classLoader);
    this._cypherStatementRegistry = createCypherRegistry(this._classLoader);
  }

  /**
   * <p>
   * </p>
   *
   * @param classLoader
   * @return
   */
  private ICypherStatementRegistry createCypherRegistry(ClassLoader classLoader) {

    //
    IClasspathScannerFactory classpathScannerFactory = ClasspathScannerFactoryBuilder.newClasspathScannerFactory()
        .registerCodeSourceClassLoaderProvider(URLClassLoader.class, cl -> cl).create();

    IClasspathScanner classpathScanner = classpathScannerFactory.createScanner(classLoader,
        ScannerBackendLoader.class.getClassLoader());

    //
    ICypherStatementRegistry cypherStatementRegistry = new CypherStatementRegistry(() -> {

      //
      List<ICypherStatement> result = new ArrayList<>();

      //
      classpathScanner.matchFiles("cypher", (name, stream, lengthBytes) -> SlizaaCypherFileParser.parse(name, stream),
          (codeSource, items) -> result.addAll(items)).scan();

      //
      return result;
    });

    return cypherStatementRegistry;
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
