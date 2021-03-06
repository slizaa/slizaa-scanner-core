/*******************************************************************************
 * Copyright (C) 2011-2017 Gerd Wuetherich (gerd@gerd-wuetherich.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gerd Wuetherich (gerd@gerd-wuetherich.de) - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.contentdefinition;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import org.slizaa.core.mvnresolver.api.IMvnCoordinate;
import org.slizaa.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.scanner.spi.contentdefinition.AbstractContentDefinitionProvider;
import org.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MvnBasedContentDefinitionProvider
    extends AbstractContentDefinitionProvider<MvnBasedContentDefinitionProvider> {

  /** - */
  private List<IMvnCoordinate>                                                       _mavenCoordinates;

  /** - */
  private final IMvnResolverService                                                  resolverService;

  /** - */
  private final IContentDefinitionProviderFactory<MvnBasedContentDefinitionProvider> _contentDefinitionProviderFactory;

  /**
   * 
   */
  @Override
  public IContentDefinitionProviderFactory<MvnBasedContentDefinitionProvider> getContentDefinitionProviderFactory() {
    return _contentDefinitionProviderFactory;
  }
  
  @Override
  public String toExternalRepresentation() {
    return _contentDefinitionProviderFactory.toExternalRepresentation(this);
  }

  /**
   * <p>
   * </p>
   *
   * @param groupId
   * @param artifactId
   * @param version
   */
  @Deprecated
  public void addArtifact(String groupId, String artifactId, String version) {
    addArtifact(String.format("%s:%s:%s", checkNotNull(groupId), checkNotNull(artifactId), checkNotNull(version)));
  }

  public void setMavenCoordinates(List<String> mavenCoordinates) {
    _mavenCoordinates = checkNotNull(mavenCoordinates.stream()
        .map(coordinate -> resolverService.parseCoordinate(coordinate)).collect(Collectors.toList()));
  }

  public IMvnCoordinate addArtifact(String coordinate) {
    IMvnCoordinate mvnCoordinate = resolverService.parseCoordinate(coordinate);
    _mavenCoordinates.add(mvnCoordinate);
    return mvnCoordinate;
  }

  public List<IMvnCoordinate> getMavenCoordinates() {
    return _mavenCoordinates;
  }

  /**
   */
  protected void onInitializeProjectContent() {

    //
    for (IMvnCoordinate mvnCoordinate : _mavenCoordinates) {

      // TODO
      File resolvedFile = resolverService.resolveArtifact(String.format("%s:%s:%s", mvnCoordinate.getGroupId(),
          mvnCoordinate.getArtifactId(), mvnCoordinate.getVersion()));

      this.createFileBasedContentDefinition(mvnCoordinate.getArtifactId(), mvnCoordinate.getVersion(),
          new File[] { resolvedFile }, null, AnalyzeMode.BINARIES_ONLY);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onDisposeProjectContent() {
    //
  }
  
  /**
   * <p>
   * Creates a new instance of type {@link MvnBasedContentDefinitionProvider}.
   * </p>
   */
  MvnBasedContentDefinitionProvider(IContentDefinitionProviderFactory<MvnBasedContentDefinitionProvider> contentDefinitionProviderFactory) {
    _mavenCoordinates = new LinkedList<>();
    resolverService = MvnResolverServiceFactoryFactory.createNewResolverServiceFactory().newMvnResolverService()
        .withDefaultRemoteRepository().create();
    _contentDefinitionProviderFactory = contentDefinitionProviderFactory;
  }

}
