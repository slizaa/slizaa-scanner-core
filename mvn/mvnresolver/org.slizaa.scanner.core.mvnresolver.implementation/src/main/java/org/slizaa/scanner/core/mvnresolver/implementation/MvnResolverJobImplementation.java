/**
 *
 */
package org.slizaa.scanner.core.mvnresolver.implementation;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverService.IMvnResolverJob;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
class MvnResolverJobImplementation implements IMvnResolverJob {

  /** - */
  private MvnResolverServiceImplementation _service;

  /** - */
  private List<String>                     _coords;

  /** - */
  private List<String>                     _exclusionPatterns;

  /** - */
  private List<String>                     _inclusionPatterns;

  /**
   * <p>
   * Creates a new instance of type {@link MvnResolverJobImplementation}.
   * </p>
   *
   * @param service
   */
  public MvnResolverJobImplementation(MvnResolverServiceImplementation service) {
    this._service = checkNotNull(service);
    this._coords = new ArrayList<>();
    this._exclusionPatterns = new ArrayList<>();
    this._inclusionPatterns = new ArrayList<>();
  }

  @Override
  public IMvnResolverJob withDependency(String coord) {
    this._coords.add(checkNotNull(coord));
    return this;
  }

  @Override
  public IMvnResolverJob withDependencies(String... coords) {
    for (String coord : checkNotNull(coords)) {
      this._coords.add(coord);
    }
    return this;
  }

  @Override
  public IMvnResolverJob withExclusionPattern(String pattern) {
    this._exclusionPatterns.add(checkNotNull(pattern));
    return this;
  }

  @Override
  public IMvnResolverJob withExclusionPatterns(String... patterns) {
    for (String pattern : checkNotNull(patterns)) {
      this._exclusionPatterns.add(pattern);
    }
    return this;
  }

  @Override
  public IMvnResolverJob withInclusionPattern(String pattern) {
    this._inclusionPatterns.add(checkNotNull(pattern));
    return this;
  }

  @Override
  public IMvnResolverJob withInclusionPattern(String... patterns) {
    for (String pattern : checkNotNull(patterns)) {
      this._inclusionPatterns.add(pattern);
    }
    return this;
  }

  @Override
  public File[] resolve() {
    return this._service.resolve(this);
  }

  @Override
  public URL[] resolveToUrlArray() {

    File[] files = resolve();

    //
    List<URL> urls = new ArrayList<>(files.length);
    for (File file : files) {
      if (file.getName().contains("org.slizaa.scanner.core.spi-api")) {
        continue;
      }
      try {
        urls.add(file.toURI().toURL());
      } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    //
    return urls.toArray(new URL[0]);
  }

  List<String> getCoords() {
    return this._coords;
  }

  List<String> getExclusionPatterns() {
    return this._exclusionPatterns;
  }

  List<String> getInclusionPatterns() {
    return this._inclusionPatterns;
  }

}
