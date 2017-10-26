package org.slizaa.addons.mvnresolver.itest;

import java.io.File;

import org.junit.Test;
import org.slizaa.addons.mvnresolver.impl.MvnResolverServiceImplementation;

public class MvnResolverServiceImplementationTest {

  @Test
  public void testIt() {

    MvnResolverServiceImplementation impl = new org.slizaa.addons.mvnresolver.impl.MvnResolverServiceImplementation();
    
    File [] files = impl.resolve("org.neo4j.test:neo4j-harness:2.3.3");
  }
}
