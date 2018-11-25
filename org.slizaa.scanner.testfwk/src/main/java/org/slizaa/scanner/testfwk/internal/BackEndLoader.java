package org.slizaa.scanner.testfwk.internal;


import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.cypherregistry.CypherRegistryUtils;
import org.slizaa.scanner.cypherregistry.CypherStatementRegistry;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.scanner.testfwk.AbstractSlizaaTestServerRule.ITestFwkBackEnd;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BackEndLoader implements ITestFwkBackEnd {

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
  public BackEndLoader(Consumer<IMvnResolverService.IMvnResolverJob> configurer) {
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
  public BackEndLoader(Consumer<IMvnResolverService.IMvnResolverJob> configurer, ClassLoader mainClassLoader) {

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
    IMvnResolverService.IMvnResolverJob mvnResolverJob = mvnResolverService.newMvnResolverJob();

    // ...configure it...
    configurer.accept(mvnResolverJob);

    // ... and create a new class loader from the result
    this._classLoader = new URLClassLoader(mvnResolverJob.resolveToUrlArray(), mainClassLoader);

    // Step 1: load services via service loader mechanism
    this._modelImporterFactory = singleService(IModelImporterFactory.class, this._classLoader);
    this._graphDbFactory = singleService(IGraphDbFactory.class, this._classLoader);
    this._parserFactories = allServices(IParserFactory.class, this._classLoader);

    // Step 2: create the cypher statement registry
    this._cypherStatementRegistry = new CypherStatementRegistry(() -> {
      return CypherRegistryUtils.getCypherStatementsFromClasspath(this._classLoader);
    });
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  public IModelImporterFactory getModelImporterFactory() {
    return this._modelImporterFactory;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  public IGraphDbFactory getGraphDbFactory() {
    return this._graphDbFactory;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  public ICypherStatementRegistry getCypherStatementRegistry() {
    return this._cypherStatementRegistry;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
  public List<IParserFactory> getParserFactories() {
    return this._parserFactories;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  @Override
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