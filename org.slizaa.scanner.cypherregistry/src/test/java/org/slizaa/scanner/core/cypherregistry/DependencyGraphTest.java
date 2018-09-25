package org.slizaa.scanner.cypherregistry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slizaa.scanner.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.cypherregistry.impl.DependencyGraph;

public class DependencyGraphTest {

  @Test
  public void test() {

    //
    DefaultCypherStatement a_1 = new DefaultCypherStatement("group", "a_1", "Match Return");
    DefaultCypherStatement a_2 = new DefaultCypherStatement("group", "a_2", "Match Return");
    DefaultCypherStatement b_1 = new DefaultCypherStatement("group", "b_1", "Match Return");
    DefaultCypherStatement b_2 = new DefaultCypherStatement("group", "b_2", "Match Return");
    DefaultCypherStatement c_1 = new DefaultCypherStatement("group", "c_1", "Match Return");
    DefaultCypherStatement c_2 = new DefaultCypherStatement("group", "c_2", "Match Return");
    DefaultCypherStatement d = new DefaultCypherStatement("group", "d", "Match Return");

    DependencyGraph<ICypherStatement> dependencyGraph = new DependencyGraph<ICypherStatement>();
    dependencyGraph.addVertex(a_1);
    dependencyGraph.addVertex(a_2);
    dependencyGraph.addVertex(b_1);
    dependencyGraph.addVertex(b_2);
    dependencyGraph.addVertex(c_1);
    dependencyGraph.addVertex(c_2);
    dependencyGraph.addVertex(d);

    dependencyGraph.addEdge(a_2, a_1);
    dependencyGraph.addEdge(b_2, b_1);
    dependencyGraph.addEdge(c_2, c_1);

    assertThat(dependencyGraph.calculateOrder()).containsSubsequence(a_1, a_2);
    assertThat(dependencyGraph.calculateOrder()).containsSubsequence(b_1, b_2);
    assertThat(dependencyGraph.calculateOrder()).containsSubsequence(c_1, c_2);
  }
}
