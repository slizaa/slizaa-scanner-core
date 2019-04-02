package org.slizaa.scanner.contentdefinition;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinition;

public class MvnBasedContentDefnitionTest {

  @Test
  public void testMvnBasedContentDefnition_1() {

    MvnBasedContentDefinitionProviderFactory factory = new MvnBasedContentDefinitionProviderFactory();

    MvnBasedContentDefinitionProvider contentDefinitionProvider = factory.emptyContentDefinitionProvider();
    contentDefinitionProvider.addArtifact("org.springframework:spring-core:5.1.6.RELEASE");
    contentDefinitionProvider.addArtifact("org.springframework:spring-beans:5.1.6.RELEASE");

    List<IContentDefinition> contentDefinitions = contentDefinitionProvider.getContentDefinitions();
    
    assertThat(contentDefinitions).hasSize(2);
    assertThat(contentDefinitions.get(0).getName()).isEqualTo("spring-core");
    assertThat(contentDefinitions.get(1).getName()).isEqualTo("spring-beans");
  }
  
  @Test
  public void testMvnBasedContentDefnition_2() {

    MvnBasedContentDefinitionProviderFactory factory = new MvnBasedContentDefinitionProviderFactory();
    
    String externalRepresentation = "org.springframework:spring-core:5.1.6.RELEASE" + System.lineSeparator() + "org.springframework:spring-beans:5.1.6.RELEASE";
    
    MvnBasedContentDefinitionProvider contentDefinitionProvider = factory.fromExternalRepresentation(externalRepresentation);

    List<IContentDefinition> contentDefinitions = contentDefinitionProvider.getContentDefinitions();
    
    assertThat(contentDefinitions).hasSize(2);
    assertThat(contentDefinitions.get(0).getName()).isEqualTo("spring-core");
    assertThat(contentDefinitions.get(1).getName()).isEqualTo("spring-beans");
  }
  
  @Test
  public void testMvnBasedContentDefnition_3() {

    MvnBasedContentDefinitionProviderFactory factory = new MvnBasedContentDefinitionProviderFactory();

    MvnBasedContentDefinitionProvider contentDefinitionProvider = factory.emptyContentDefinitionProvider();
    contentDefinitionProvider.addArtifact("org.springframework:spring-core:5.1.6.RELEASE");
    contentDefinitionProvider.addArtifact("org.springframework:spring-beans:5.1.6.RELEASE");

    String externalRepresentation = factory.toExternalRepresentation(contentDefinitionProvider);
    
    MvnBasedContentDefinitionProvider contentDefinitionProvider2 = factory.fromExternalRepresentation(externalRepresentation);
    List<IContentDefinition> contentDefinitions = contentDefinitionProvider2.getContentDefinitions();
    
    assertThat(contentDefinitions).hasSize(2);
    assertThat(contentDefinitions.get(0).getName()).isEqualTo("spring-core");
    assertThat(contentDefinitions.get(1).getName()).isEqualTo("spring-beans");
  }
}
