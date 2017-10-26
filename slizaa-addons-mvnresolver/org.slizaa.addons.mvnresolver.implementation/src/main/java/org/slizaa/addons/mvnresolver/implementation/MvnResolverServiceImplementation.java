package org.slizaa.addons.mvnresolver.implementation;

import java.io.File;

import org.slizaa.addons.mvnresolver.api.MvnResolverService;
import org.slizaa.addons.mvnresolver.implementation.internal.TransitiveDependenciesResolver;

public class MvnResolverServiceImplementation implements MvnResolverService {

  @Override
  public File[] resolve(String coords) {
    return TransitiveDependenciesResolver.resolve(coords);
  }
}
