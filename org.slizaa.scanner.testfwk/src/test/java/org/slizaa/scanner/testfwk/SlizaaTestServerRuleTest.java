/**
 *
 */
package org.slizaa.scanner.testfwk;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 */
@Ignore("This test has an invalid runtime dependency to 'org.slizaa.neo4j' and 'org.slizaa.jtype'.")
public class SlizaaTestServerRuleTest {

  // @formatter:off

  @ClassRule
  public static SlizaaTestServerRule slizaaTestServerRule = new SlizaaTestServerRule(

      // create the content provider
      ContentDefinitionProviderFactory.simpleBinaryMvnArtifact("com.google.guava", "guava", "25.1-jre"),

      // configure the backend
      job -> job.withDependency("org.slizaa.neo4j:org.slizaa.neo4j.importer:1.0.0-SNAPSHOT")
                .withDependency("org.slizaa.neo4j:org.slizaa.neo4j.graphdbfactory:1.0.0-SNAPSHOT")
                .withDependency("org.slizaa.jtype:org.slizaa.jtype.scanner:1.0.0-SNAPSHOT")
                .withExclusionPattern("*:org.slizaa.scanner.spi-api")
                .withExclusionPattern("*:jdk.tools"));

  // @formatter:on

  @Test
  public void testIt() {

    // do nothing
  }
}
