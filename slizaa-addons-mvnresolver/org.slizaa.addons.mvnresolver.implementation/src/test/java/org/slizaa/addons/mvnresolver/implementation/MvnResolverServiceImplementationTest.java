package org.slizaa.addons.mvnresolver.implementation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

public class MvnResolverServiceImplementationTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void testMvnResolverServiceImplementation() {

    MvnResolverServiceImplementation implementation = new MvnResolverServiceImplementation();
    
    File [] files = implementation.resolve("org.neo4j.test:neo4j-harness:2.3.3");
    
    assertThat(files).hasSize(75);
  }
}
