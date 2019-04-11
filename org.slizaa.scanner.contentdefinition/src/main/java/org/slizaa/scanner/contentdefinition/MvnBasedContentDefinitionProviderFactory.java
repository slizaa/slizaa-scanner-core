package org.slizaa.scanner.contentdefinition;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Scanner;
import java.util.stream.Collectors;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;

public class MvnBasedContentDefinitionProviderFactory
    implements IContentDefinitionProviderFactory<MvnBasedContentDefinitionProvider> {

  private static final String DELIMITER = ",";

  @Override
  public String getFactoryId() {
    return MvnBasedContentDefinitionProviderFactory.class.getName();
  }

  @Override
  public String getName() {
    return "Maven Based";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public MvnBasedContentDefinitionProvider emptyContentDefinitionProvider() {
    return new MvnBasedContentDefinitionProvider(this);
  }

  @Override
  public String toExternalRepresentation(MvnBasedContentDefinitionProvider contentDefinitionProvider) {
    return contentDefinitionProvider.getMavenCoordinates().stream()
        .map(mvnCoordinate -> mvnCoordinate.toCanonicalForm()).collect(Collectors.joining(DELIMITER));
  }

  @Override
  public MvnBasedContentDefinitionProvider fromExternalRepresentation(String externalRepresentation) {

    checkNotNull(externalRepresentation);

    MvnBasedContentDefinitionProvider contentDefinitionProvider = emptyContentDefinitionProvider();

    String[] parts = externalRepresentation.split(DELIMITER);

    for (String part : parts) {
      part = part.trim();
      if (!part.isEmpty()) {
        contentDefinitionProvider.addArtifact(part);
      }
    }

    return contentDefinitionProvider;
  }
}
