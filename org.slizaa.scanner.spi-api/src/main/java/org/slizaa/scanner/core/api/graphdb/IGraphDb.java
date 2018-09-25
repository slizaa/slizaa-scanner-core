/*******************************************************************************
 * Copyright (C) 2011-2017 Gerd Wuetherich (gerd@gerd-wuetherich.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gerd Wuetherich (gerd@gerd-wuetherich.de) - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.api.graphdb;

/**
 * <p>
 * Represents a (local) database that can be accessed via the BOLT protocol.
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IGraphDb extends AutoCloseable {

  /**
   * <p>
   * Returns the port that clients should use to connect against this graph database via the BOLT protocol..
   * </p>
   *
   * @return the database port
   */
  int getPort();

  /**
   * <p>
   * Shuts the database down.
   * </p>
   */
  void shutdown();

  /**
   * <p>
   * Returns the user object with the specified type if one exists, or <code>null</code> otherwise.
   * </p>
   *
   * @param userObject
   *          the associated user object
   * @return the user object with the specified type.
   */
  <T> T getUserObject(Class<T> userObject);

  /**
   * <p>
   * Return <code>true</code> if the graph has an associated user object with the given type, <code>false</code>
   * otherwise.
   * </p>
   *
   * @param type
   *          the type of the user object
   * @return <code>true</code> if the graph has an associated user object with the given type, <code>false</code>
   *         otherwise.
   */
  <T> boolean hasUserObject(Class<T> type);
}
