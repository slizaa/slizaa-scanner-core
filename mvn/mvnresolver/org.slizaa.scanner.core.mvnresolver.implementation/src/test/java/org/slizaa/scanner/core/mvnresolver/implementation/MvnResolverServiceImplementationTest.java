package org.slizaa.scanner.core.mvnresolver.implementation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverService;
import org.slizaa.scanner.core.mvnresolver.implementation.MvnResolverServiceFactoryImplementation;

public class MvnResolverServiceImplementationTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void testMvnResolverServiceImplementation() {

    IMvnResolverService implementation = new MvnResolverServiceFactoryImplementation().newMvnResolverService().create();

    File[] files = implementation.resolve("org.neo4j.test:neo4j-harness:2.3.3");

    assertThat(files).hasSize(75);
  }
}
