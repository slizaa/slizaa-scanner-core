package org.slizaa.scanner.core.mvnresolver;

import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverServiceFactory;
import org.slizaa.scanner.core.mvnresolver.uber.UberServiceInvocationHandler;

public class MvnResolverServiceFactoryFactory {

  public static IMvnResolverServiceFactory createNewResolverServiceFactory() {

    return UberServiceInvocationHandler.createNewResolverService(IMvnResolverServiceFactory.class, (jcl) -> {

      // load the class...
      Class<?> clazz = jcl
          .loadClass("org.slizaa.scanner.core.mvnresolver.implementation.MvnResolverServiceFactoryImplementation");
                      
      // ... and create a new instance.
      return (IMvnResolverServiceFactory) clazz.newInstance();
    });
  }

}
