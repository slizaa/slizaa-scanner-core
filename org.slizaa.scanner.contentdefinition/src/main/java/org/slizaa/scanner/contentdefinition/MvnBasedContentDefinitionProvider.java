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
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MvnBasedContentDefinitionProvider extends AbstractContentDefinitionProvider
		implements IContentDefinitionProvider {

	/** - */
	private List<String> _mavenCoordinates;

	private final IMvnResolverService resolverService;

	/**
	 * <p>
	 * Creates a new instance of type {@link MvnBasedContentDefinitionProvider}.
	 * </p>
	 */
	public MvnBasedContentDefinitionProvider() {
		_mavenCoordinates = new LinkedList<>();
		resolverService = MvnResolverServiceFactoryFactory.createNewResolverServiceFactory()
				.newMvnResolverService().withDefaultRemoteRepository().create();
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
		_mavenCoordinates
				.add(String.format("%s:%s:%s", checkNotNull(groupId), checkNotNull(artifactId), checkNotNull(version)));
	}
	
	public IMvnCoordinate addArtifact(String coordinate) {
		_mavenCoordinates.add(coordinate);
		return resolverService.parseCoordinate(coordinate);
	}
	
	public List<IMvnCoordinate> getMavenCoordinates() {
    return _mavenCoordinates.stream().map(coordinate -> resolverService.parseCoordinate(coordinate)).collect(Collectors.toList());
  }

  public void setMavenCoordinates(List<String> mavenCoordinates) {
    _mavenCoordinates = checkNotNull(mavenCoordinates);
  }

  /**
	 */
	protected void onInitializeProjectContent() {

		//
		for (String coord : _mavenCoordinates) {

			// TODO

			IMvnCoordinate mvnCoordinate = resolverService.parseCoordinate(coord);
			File resolvedFile = resolverService.resolveArtifact(coord);

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
}
