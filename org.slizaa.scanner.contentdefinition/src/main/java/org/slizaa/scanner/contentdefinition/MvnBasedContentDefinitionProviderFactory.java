package org.slizaa.scanner.contentdefinition;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Scanner;
import java.util.stream.Collectors;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;

public class MvnBasedContentDefinitionProviderFactory
    implements IContentDefinitionProviderFactory<MvnBasedContentDefinitionProvider> {

  @Override
  public String getFactoryId() {
    return IContentDefinitionProviderFactory.class.getName();
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
        .map(mvnCoordinate -> mvnCoordinate.toCanonicalForm()).collect(Collectors.joining(System.lineSeparator()));
  }

  @Override
  public MvnBasedContentDefinitionProvider fromExternalRepresentation(String externalRepresentation) {

    checkNotNull(externalRepresentation);

    MvnBasedContentDefinitionProvider contentDefinitionProvider = emptyContentDefinitionProvider();

    try (Scanner scanner = new Scanner(externalRepresentation)) {

      while (scanner.hasNextLine()) {
        String artifact = scanner.nextLine();
        if (!artifact.isEmpty()) {
          contentDefinitionProvider.addArtifact(artifact);
        }
      }
    }

    return contentDefinitionProvider;
  }
}
