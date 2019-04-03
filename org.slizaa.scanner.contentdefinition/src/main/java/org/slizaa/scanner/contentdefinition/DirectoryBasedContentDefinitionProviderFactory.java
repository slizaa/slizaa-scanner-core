package org.slizaa.scanner.contentdefinition;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;

public class DirectoryBasedContentDefinitionProviderFactory implements IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> {

  @Override
  public String getFactoryId() {
    return DirectoryBasedContentDefinitionProviderFactory.class.getName();
  }

  @Override
  public String getName() {
    return "Directory based";
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public DirectoryBasedContentDefinitionProvider emptyContentDefinitionProvider() {
    return new DirectoryBasedContentDefinitionProvider(this);
  }

  @Override
  public String toExternalRepresentation(DirectoryBasedContentDefinitionProvider contentDefinitionProvider) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DirectoryBasedContentDefinitionProvider fromExternalRepresentation(String externalRepresentation) {
    // TODO Auto-generated method stub
    return null;
  }

  
}
