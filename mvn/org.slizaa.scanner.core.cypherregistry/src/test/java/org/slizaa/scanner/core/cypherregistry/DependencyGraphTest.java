package org.slizaa.scanner.core.cypherregistry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.cypherregistry.impl.DependencyGraph;

public class DependencyGraphTest {

  @Test
  public void test() {

    //
    DefaultCypherStatement a = new DefaultCypherStatement("group", "a", "Match Return");
    DefaultCypherStatement b = new DefaultCypherStatement("group", "b", "Match Return");
    DefaultCypherStatement c = new DefaultCypherStatement("group", "c", "Match Return");

    DependencyGraph<ICypherStatement> dependencyGraph = new DependencyGraph<ICypherStatement>();

    dependencyGraph.addEdge(a, b);
    dependencyGraph.addEdge(a, c);
    dependencyGraph.addEdge(b, c);

    assertThat(dependencyGraph.calculateOrder()).containsExactly(c, b, a);
  }
}
