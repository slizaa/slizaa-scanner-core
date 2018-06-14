package org.slizaa.scanner.core.mvnresolver.implementation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.slizaa.scanner.core.mvnresolver.api.IMvnResolverService;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MvnResolverServiceImplementationTest {

  /** - */
  private IMvnResolverService _mvnResolverService;

  /**
   * <p>
   * </p>
   */
  @Before
  public void init() {

    // create the resolver service
    this._mvnResolverService = new MvnResolverServiceFactoryImplementation().newMvnResolverService().create();
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void test_1() {

    //
    File[] files = this._mvnResolverService.resolve("org.neo4j.test:neo4j-harness:2.3.3");
    List<String> names = Arrays.asList(files).stream().map(file -> file.getName()).collect(Collectors.toList());

    //
    assertThat(files).hasSize(75);
    assertThat(names).contains("neo4j-harness-2.3.3.jar");
    assertThat(names).contains("neo4j-2.3.3.jar");
    assertThat(names).contains("neo4j-kernel-2.3.3.jar");
    assertThat(names).contains("neo4j-lucene-index-2.3.3.jar");
    assertThat(names).contains("lucene-core-3.6.2.jar");
    assertThat(names).contains("neo4j-graph-algo-2.3.3.jar");
    assertThat(names).contains("neo4j-udc-2.3.3.jar");
    assertThat(names).contains("neo4j-graph-matching-2.3.3.jar");
    assertThat(names).contains("neo4j-cypher-2.3.3.jar");
    assertThat(names).contains("scala-library-2.11.7.jar");
    assertThat(names).contains("scala-reflect-2.11.7.jar");
    assertThat(names).contains("scala-parser-combinators_2.11-1.0.4.jar");
    assertThat(names).contains("neo4j-codegen-2.3.3.jar");
    assertThat(names).contains("asm-5.0.2.jar");
    assertThat(names).contains("neo4j-cypher-compiler-1.9_2.11-2.0.5.jar");
    assertThat(names).contains("neo4j-cypher-compiler-2.2_2.11-2.2.6.jar");
    assertThat(names).contains("neo4j-cypher-compiler-2.3-2.3.3.jar");
    assertThat(names).contains("neo4j-cypher-frontend-2.3-2.3.3.jar");
    assertThat(names).contains("parboiled-scala_2.11-1.1.7.jar");
    assertThat(names).contains("parboiled-core-1.1.7.jar");
    assertThat(names).contains("opencsv-2.3.jar");
    assertThat(names).contains("concurrentlinkedhashmap-lru-1.4.2.jar");
    assertThat(names).contains("neo4j-jmx-2.3.3.jar");
    assertThat(names).contains("neo4j-consistency-check-2.3.3.jar");
    assertThat(names).contains("neo4j-consistency-check-legacy-2.3.3.jar");
    assertThat(names).contains("neo4j-server-2.3.3.jar");
    assertThat(names).contains("neo4j-2.3.3.pom");
    assertThat(names).contains("server-api-2.3.3.jar");
    assertThat(names).contains("jsr311-api-1.1.2.r612.jar");
    assertThat(names).contains("neo4j-browser-2.3.3.jar");
    assertThat(names).contains("neo4j-shell-2.3.3.jar");
    assertThat(names).contains("jline-2.12.jar");
    assertThat(names).contains("logback-classic-1.1.2.jar");
    assertThat(names).contains("logback-core-1.1.2.jar");
    assertThat(names).contains("slf4j-api-1.7.6.jar");
    assertThat(names).contains("logback-access-1.1.2.jar");
    assertThat(names).contains("jetty-server-9.2.9.v20150224.jar");
    assertThat(names).contains("javax.servlet-api-3.1.0.jar");
    assertThat(names).contains("jetty-http-9.2.9.v20150224.jar");
    assertThat(names).contains("jetty-util-9.2.9.v20150224.jar");
    assertThat(names).contains("jetty-io-9.2.9.v20150224.jar");
    assertThat(names).contains("jetty-webapp-9.2.9.v20150224.jar");
    assertThat(names).contains("jetty-xml-9.2.9.v20150224.jar");
    assertThat(names).contains("jetty-servlet-9.2.9.v20150224.jar");
    assertThat(names).contains("jetty-security-9.2.9.v20150224.jar");
    assertThat(names).contains("jersey-server-1.19.jar");
    assertThat(names).contains("jersey-servlet-1.19.jar");
    assertThat(names).contains("commons-configuration-1.10.jar");
    assertThat(names).contains("commons-lang-2.6.jar");
    assertThat(names).contains("commons-logging-1.1.1.jar");
    assertThat(names).contains("netty-all-4.0.28.Final.jar");
    assertThat(names).contains("commons-digester-2.1.jar");
    assertThat(names).contains("commons-beanutils-1.8.3.jar");
    assertThat(names).contains("commons-io-2.4.jar");
    assertThat(names).contains("jackson-jaxrs-1.9.13.jar");
    assertThat(names).contains("jackson-core-asl-1.9.13.jar");
    assertThat(names).contains("jackson-mapper-asl-1.9.13.jar");
    assertThat(names).contains("rrd4j-2.2.jar");
    assertThat(names).contains("rhino-1.7R4.jar");
    assertThat(names).contains("bcprov-jdk15on-1.52.jar");
    assertThat(names).contains("bcpkix-jdk15on-1.52.jar");
    assertThat(names).contains("jersey-multipart-1.19.jar");
    assertThat(names).contains("mimepull-1.9.3.jar");
    assertThat(names).contains("neo4j-kernel-2.3.3-tests.jar");
    assertThat(names).contains("neo4j-primitive-collections-2.3.3.jar");
    assertThat(names).contains("neo4j-function-2.3.3.jar");
    assertThat(names).contains("neo4j-io-2.3.3.jar");
    assertThat(names).contains("neo4j-csv-2.3.3.jar");
    assertThat(names).contains("neo4j-logging-2.3.3.jar");
    assertThat(names).contains("neo4j-io-2.3.3-tests.jar");
    assertThat(names).contains("neo4j-unsafe-2.3.3.jar");
    assertThat(names).contains("commons-lang3-3.3.2.jar");
    assertThat(names).contains("jersey-client-1.19.jar");
    assertThat(names).contains("jersey-core-1.19.jar");
    assertThat(names).contains("neo4j-server-2.3.3-tests.jar");
  }

  @Test
  public void test_2() {

    //
    File[] files = this._mvnResolverService.resolve("net.bytebuddy:byte-buddy:jar:1.8.5",
        "org.mockito:mockito-core:jar:2.18.3");
    List<String> names = Arrays.asList(files).stream().map(file -> file.getName()).collect(Collectors.toList());

    //
    assertThat(names).hasSize(4);
    assertThat(names).contains("byte-buddy-1.8.5.jar");
    assertThat(names).contains("mockito-core-2.18.3.jar");
    assertThat(names).contains("byte-buddy-agent-1.8.5.jar");
    assertThat(names).contains("objenesis-2.6.jar");

  }

  @Test
  public void test_3() {

    //
    File[] files = this._mvnResolverService.newMvnResolverJob()
        .withDependencies("net.bytebuddy:byte-buddy:jar:1.8.5", "org.mockito:mockito-core:jar:2.18.3").resolve();
    List<String> names = Arrays.asList(files).stream().map(file -> file.getName()).collect(Collectors.toList());

    //
    assertThat(names).hasSize(4);
    assertThat(names).contains("byte-buddy-1.8.5.jar");
    assertThat(names).contains("mockito-core-2.18.3.jar");
    assertThat(names).contains("byte-buddy-agent-1.8.5.jar");
    assertThat(names).contains("objenesis-2.6.jar");
  }

  @Test
  public void test_4() {

    //
    File[] files = this._mvnResolverService.newMvnResolverJob()
        .withDependencies("net.bytebuddy:byte-buddy:jar:1.8.5", "org.mockito:mockito-core:jar:2.18.3")
        .withExclusionPattern("*:byte-buddy-*").resolve();
    List<String> names = Arrays.asList(files).stream().map(file -> file.getName()).collect(Collectors.toList());

    //
    assertThat(names).hasSize(3);
    assertThat(names).contains("byte-buddy-1.8.5.jar");
    assertThat(names).contains("mockito-core-2.18.3.jar");
    assertThat(names).contains("objenesis-2.6.jar");

  }

  // @Test
  // public void test_5() {
  //
  // //
  // File[] files = this._mvnResolverService.newMvnResolverJob()
  // .withDependency("org.slizaa.scanner.neo4j:org.slizaa.scanner.neo4j.importer:1.0.0-SNAPSHOT")
  // .withDependency("org.slizaa.scanner.neo4j:org.slizaa.scanner.neo4j.graphdbfactory:1.0.0-SNAPSHOT")
  // .withExclusionPattern("*:org.slizaa.scanner.core.spi-api").withExclusionPattern("*:jdk.tools").resolve();
  // List<String> names = Arrays.asList(files).stream().map(file -> file.getName()).collect(Collectors.toList());
  //
  // assertThat(names).hasSize(103);
  // assertThat(names).contains("org.slizaa.scanner.neo4j.importer-1.0.0-SNAPSHOT.jar");
  // assertThat(names).contains("neo4j-3.4.0.jar");
  // assertThat(names).contains("neo4j-kernel-3.4.0.jar");
  // assertThat(names).contains("neo4j-graphdb-api-3.4.0.jar");
  // assertThat(names).contains("neo4j-resource-3.4.0.jar");
  // assertThat(names).contains("neo4j-procedure-api-3.4.0.jar");
  // assertThat(names).contains("neo4j-kernel-api-3.4.0.jar");
  // assertThat(names).contains("neo4j-common-3.4.0.jar");
  // assertThat(names).contains("neo4j-collections-3.4.0.jar");
  // assertThat(names).contains("neo4j-primitive-collections-3.4.0.jar");
  // assertThat(names).contains("neo4j-unsafe-3.4.0.jar");
  // assertThat(names).contains("neo4j-io-3.4.0.jar");
  // assertThat(names).contains("neo4j-csv-3.4.0.jar");
  // assertThat(names).contains("neo4j-logging-3.4.0.jar");
  // assertThat(names).contains("neo4j-lucene-upgrade-3.4.0.jar");
  // assertThat(names).contains("lucene-backward-codecs-5.5.0.jar");
  // assertThat(names).contains("neo4j-configuration-3.4.0.jar");
  // assertThat(names).contains("neo4j-index-3.4.0.jar");
  // assertThat(names).contains("neo4j-spatial-index-3.4.0.jar");
  // assertThat(names).contains("neo4j-lucene-index-3.4.0.jar");
  // assertThat(names).contains("lucene-analyzers-common-5.5.5.jar");
  // assertThat(names).contains("lucene-core-5.5.5.jar");
  // assertThat(names).contains("lucene-queryparser-5.5.5.jar");
  // assertThat(names).contains("lucene-codecs-5.5.5.jar");
  // assertThat(names).contains("neo4j-graph-algo-3.4.0.jar");
  // assertThat(names).contains("neo4j-udc-3.4.0.jar");
  // assertThat(names).contains("neo4j-cypher-3.4.0.jar");
  // assertThat(names).contains("scala-library-2.11.12.jar");
  // assertThat(names).contains("scala-reflect-2.11.12.jar");
  // assertThat(names).contains("neo4j-graph-matching-3.1.6.jar");
  // assertThat(names).contains("neo4j-codegen-3.4.0.jar");
  // assertThat(names).contains("asm-util-6.0.jar");
  // assertThat(names).contains("asm-analysis-6.0.jar");
  // assertThat(names).contains("asm-tree-6.0.jar");
  // assertThat(names).contains("neo4j-cypher-compiler-2.3-2.3.12.jar");
  // assertThat(names).contains("neo4j-cypher-frontend-2.3-2.3.12.jar");
  // assertThat(names).contains("concurrentlinkedhashmap-lru-1.4.2.jar");
  // assertThat(names).contains("neo4j-cypher-compiler-3.1-3.1.8.jar");
  // assertThat(names).contains("neo4j-cypher-frontend-3.1-3.1.8.jar");
  // assertThat(names).contains("caffeine-2.3.3.jar");
  // assertThat(names).contains("neo4j-cypher-compiler-3.3-3.3.5.jar");
  // assertThat(names).contains("neo4j-cypher-frontend-3.3-3.3.5.jar");
  // assertThat(names).contains("neo4j-cypher-ir-3.3-3.3.5.jar");
  // assertThat(names).contains("neo4j-cypher-logical-plans-3.3-3.3.5.jar");
  // assertThat(names).contains("neo4j-cypher-util-3.4-3.4.0.jar");
  // assertThat(names).contains("neo4j-cypher-planner-3.4-3.4.0.jar");
  // assertThat(names).contains("openCypher-frontend-1-3.4.0.jar");
  // assertThat(names).contains("neo4j-cypher-expression-3.4-3.4.0.jar");
  // assertThat(names).contains("neo4j-cypher-ir-3.4-3.4.0.jar");
  // assertThat(names).contains("neo4j-cypher-logical-plans-3.4-3.4.0.jar");
  // assertThat(names).contains("neo4j-cypher-planner-spi-3.4-3.4.0.jar");
  // assertThat(names).contains("neo4j-cypher-runtime-util-3.4.0.jar");
  // assertThat(names).contains("neo4j-cypher-interpreted-runtime-3.4.0.jar");
  // assertThat(names).contains("parboiled-scala_2.11-1.1.7.jar");
  // assertThat(names).contains("parboiled-core-1.1.7.jar");
  // assertThat(names).contains("opencsv-2.3.jar");
  // assertThat(names).contains("neo4j-jmx-3.4.0.jar");
  // assertThat(names).contains("neo4j-consistency-check-3.4.0.jar");
  // assertThat(names).contains("neo4j-command-line-3.4.0.jar");
  // assertThat(names).contains("commons-text-1.3.jar");
  // assertThat(names).contains("neo4j-dbms-3.4.0.jar");
  // assertThat(names).contains("neo4j-import-tool-3.4.0.jar");
  // assertThat(names).contains("jProcesses-1.6.4.jar");
  // assertThat(names).contains("WMI4Java-1.6.1.jar");
  // assertThat(names).contains("jPowerShell-1.9.jar");
  // assertThat(names).contains("commons-compress-1.16.1.jar");
  // assertThat(names).contains("objenesis-2.6.jar");
  // assertThat(names).contains("slf4j-api-1.7.25.jar");
  // assertThat(names).contains("org.slizaa.scanner.neo4j.graphdbfactory-1.0.0-SNAPSHOT.jar");
  // assertThat(names).contains("org.eclipse.equinox.common-3.9.0.jar");
  // assertThat(names).contains("org.slizaa.scanner.neo4j.apoc-1.0.0-SNAPSHOT.jar");
  // assertThat(names).contains("server-api-3.4.0.jar");
  // assertThat(names).contains("jsr311-api-1.1.2.r612.jar");
  // assertThat(names).contains("commons-configuration-1.10.jar");
  // assertThat(names).contains("commons-logging-1.1.1.jar");
  // assertThat(names).contains("guava-21.0.jar");
  // assertThat(names).contains("gson-2.5.jar");
  // assertThat(names).contains("neo4j-bolt-3.4.0.jar");
  // assertThat(names).contains("neo4j-values-3.4.0.jar");
  // assertThat(names).contains("neo4j-ssl-3.4.0.jar");
  // assertThat(names).contains("netty-all-4.1.24.Final.jar");
  // assertThat(names).contains("jackson-mapper-asl-1.9.13.jar");
  // assertThat(names).contains("jackson-core-asl-1.9.13.jar");
  // assertThat(names).contains("commons-lang3-3.7.jar");
  // assertThat(names).contains("bcpkix-jdk15on-1.59.jar");
  // assertThat(names).contains("bcprov-jdk15on-1.59.jar");
  // assertThat(names).contains("apoc-3.4.0.1.jar");
  // assertThat(names).contains("commons-codec-1.9.jar");
  // assertThat(names).contains("json-path-2.2.0.jar");
  // assertThat(names).contains("json-smart-2.2.1.jar");
  // assertThat(names).contains("accessors-smart-1.1.jar");
  // assertThat(names).contains("asm-5.0.3.jar");
  // assertThat(names).contains("HdrHistogram-2.1.9.jar");
  // assertThat(names).contains("neo4j-java-driver-1.4.4.jar");
  // assertThat(names).contains("jldap-2009-10-07.jar");
  // assertThat(names).contains("jackson-databind-2.9.0.jar");
  // assertThat(names).contains("jackson-annotations-2.9.0.jar");
  // assertThat(names).contains("jackson-core-2.9.0.jar");
  // assertThat(names).contains("javafaker-0.10.jar");
  // assertThat(names).contains("commons-lang-2.6.jar");
  // assertThat(names).contains("generex-1.0.1.jar");
  // assertThat(names).contains("automaton-1.11-8.jar");
  // assertThat(names).contains("commons-math3-3.6.1.jar");
  // }
}
