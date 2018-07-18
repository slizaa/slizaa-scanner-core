/**
 *
 */
package org.slizaa.scanner.core.testfwk;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import org.slizaa.core.classpathscanner.ClasspathScannerFactoryBuilder;
import org.slizaa.core.classpathscanner.IClasspathScanner;
import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.core.mvnresolver.api.IMvnResolverService.IMvnResolverJob;
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
public class BackendLoader {

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
   * Creates a new instance of type {@link BackendLoader}.
   * </p>
   *
   * @param configurer
   */
  public BackendLoader(Consumer<IMvnResolverJob> configurer) {
    this(configurer, BackendLoader.class.getClassLoader());
  }

  /**
   * <p>
   * Creates a new instance of type {@link BackendLoader}.
   * </p>
   *
   * @param configurer
   * @param mainClassLoader
   */
  public BackendLoader(Consumer<IMvnResolverJob> configurer, ClassLoader mainClassLoader) {

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
        .createScanner(this._classLoader, BackendLoader.class.getClassLoader());

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
