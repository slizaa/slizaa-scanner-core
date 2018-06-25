package org.slizaa.scanner.core.cypherregistry.impl;

/**
 * <p>
 * Interface for a vertex renderer. A vertex renderer is used to create a custom string representation of a vertex for
 * further usage in an exception message.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @param <T>
 *          the type of the vertices
 */
public interface VertexRenderer<T> {

  /**
   * <p>
   * Must return an not-null string that represents the given vertex.
   * </p>
   * 
   * @param vertex
   *          the vertex to render.
   * @return must return an not-null string that represents the given vertex.
   */
  String renderVertex(T vertex);

} /* ENDINTERFACE */