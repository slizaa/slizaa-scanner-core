package org.slizaa.scanner.core.mvnresolver.implementation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverService;

public class MvnResolverServiceImplementationTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void testMvnResolverServiceImplementation() {

    IMvnResolverService implementation = new MvnResolverServiceFactoryImplementation().newMvnResolverService().create();

    File[] files = implementation.resolve("org.neo4j.test:neo4j-harness:2.3.3");

    for (File file : files) {
      System.out.println(file.getName());
    }

    assertThat(files).hasSize(75);
  }

  @Test
  public void test_2() {

    IMvnResolverService implementation = new MvnResolverServiceFactoryImplementation().newMvnResolverService().create();

    File[] files = implementation.resolve("net.bytebuddy:byte-buddy:jar:1.8.5", "org.mockito:mockito-core:jar:2.18.3");

    for (File file : files) {
      System.out.println(file.getName());
    }

    assertThat(files).hasSize(4);
  }

  @Test
  public void test_3() {

    IMvnResolverService implementation = new MvnResolverServiceFactoryImplementation().newMvnResolverService().create();

    File[] files = implementation.newMvnResolverJob()
        .withDependencies("net.bytebuddy:byte-buddy:jar:1.8.5", "org.mockito:mockito-core:jar:2.18.3").resolve();

    for (File file : files) {
      System.out.println(file.getName());
    }

    assertThat(files).hasSize(4);
  }

  @Test
  public void test_4() {

    IMvnResolverService implementation = new MvnResolverServiceFactoryImplementation().newMvnResolverService().create();

    File[] files = implementation.newMvnResolverJob()
        .withDependencies("net.bytebuddy:byte-buddy:jar:1.8.5", "org.mockito:mockito-core:jar:2.18.3")
        .withExclusionPattern("*:byte-buddy-*").resolve();

    for (File file : files) {
      System.out.println(file.getName());
    }

    assertThat(files).hasSize(3);
  }
}
