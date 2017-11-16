package org.slizaa.scanner.core.cypherregistry;

import org.junit.Test;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.cypherregistry.impl.DependencyGraph;

public class DependencyGraphTest {

  @Test
  public void test() {
    
    //
    DefaultCypherStatement a = new DefaultCypherStatement("group", "a");
    DefaultCypherStatement b = new DefaultCypherStatement("group", "b");
    DefaultCypherStatement c = new DefaultCypherStatement("group", "c");
    
    DependencyGraph<ICypherStatement> dependencyGraph = new DependencyGraph<ICypherStatement>();
    
    dependencyGraph.addEdge(a, b);
    dependencyGraph.addEdge(a, c);
    dependencyGraph.addEdge(b, c);
    
    System.out.println(dependencyGraph.calculateOrder());
  }
}
