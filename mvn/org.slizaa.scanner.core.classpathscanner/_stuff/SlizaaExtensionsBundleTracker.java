package org.slizaa.scanner.core.classpathscanner.internal.osgi;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.BundleTracker;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.annotations.SlizaaMappingProvider;
import org.slizaa.rcp.workbench.core.model.ModelFactory;
import org.slizaa.rcp.workbench.core.model.SlizaaExtensionBundle;
import org.slizaa.rcp.workbench.core.model.SlizaaExtensionBundleExtension;
import org.slizaa.scanner.core.classpathscanner.ClasspathScannerFactoryBuilder;
import org.slizaa.scanner.core.classpathscanner.IClassAnnotationMatchHandler;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerFactory;
import org.slizaa.scanner.core.cypherregistry.SlizaaCypherFileParser;
import org.slizaa.scanner.core.spi.annotations.ParserFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
public class SlizaaExtensionsBundleTracker extends BundleTracker<SlizaaExtensionBundle> {

  /** - */
  private IClasspathScannerFactory _classpathScannerFactory;

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaExtensionsBundleTracker}.
   * </p>
   *
   * @param context
   */
  public SlizaaExtensionsBundleTracker(BundleContext context) {
    super(context, Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE, null);

    //
    this._classpathScannerFactory = ClasspathScannerFactoryBuilder.newClasspathScannerFactory()

        //
        .registerCodeSourceClassLoaderProvider(Bundle.class, (b) -> {
          return b.adapt(BundleWiring.class).getClassLoader();
        })

        //
        .create();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SlizaaExtensionBundle addingBundle(Bundle bundle, BundleEvent event) {

    //
    String slizaaExtension = bundle.getHeaders().get(IClasspathScannerFactory.SLIZAA_EXTENSION_BUNDLE_HEADER);

    //
    if (slizaaExtension != null && !slizaaExtension.isEmpty()) {

      // create the result
      SlizaaExtensionBundle slizaaExtensionBundle = ModelFactory.INSTANCE.createSlizaaExtensionBundle();

      // scan the bundle
      this._classpathScannerFactory

          //
          .createScanner(bundle)
          
          // TODO: Dynamic!
          // parser factory
          .matchClassesWithAnnotation(ParserFactory.class,
              createClassAnnotationMatchHandler(slizaaExtensionBundle, ParserFactory.class))

          // TODO: Dynamic!
          // mapping provider
          .matchClassesWithAnnotation(SlizaaMappingProvider.class,
              createClassAnnotationMatchHandler(slizaaExtensionBundle, SlizaaMappingProvider.class))
          
          // TODO: Dynamic!
          // cypher extensions
          .matchFiles("cypher",
              (relativePath, inputStream, lengthBytes) -> SlizaaCypherFileParser.parse(relativePath, inputStream),
              (b, contentList) -> slizaaExtensionBundle.getDefinedCypherStatements().addAll(contentList))

          //
          .scan();

      //
      return slizaaExtensionBundle;
    }
    //
    else {
      return null;
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param slizaaExtensionBundle
   * @return
   */
  private IClassAnnotationMatchHandler createClassAnnotationMatchHandler(SlizaaExtensionBundle slizaaExtensionBundle,
      Class<? extends Annotation> annotationType) {

    //
    return (b, exts) -> {

      //
      List<SlizaaExtensionBundleExtension> bundleExtensions = exts.stream().map(clazz -> {
        SlizaaExtensionBundleExtension bundleExtension = ModelFactory.INSTANCE.createSlizaaExtensionBundleExtension();
        bundleExtension.setAnnotationType(annotationType);
        bundleExtension.setType(clazz);
        return bundleExtension;
      }).collect(Collectors.toList());

      //
      if (!bundleExtensions.isEmpty()) {

        List<SlizaaExtensionBundleExtension> extensionBundleExtensions = slizaaExtensionBundle.getDefinedExtensions()
            .computeIfAbsent(annotationType, k -> new ArrayList<>());

        extensionBundleExtensions.addAll(bundleExtensions);
        slizaaExtensionBundle.getDefinedExtensions().put(annotationType, extensionBundleExtensions);
      }
    };
  }
}