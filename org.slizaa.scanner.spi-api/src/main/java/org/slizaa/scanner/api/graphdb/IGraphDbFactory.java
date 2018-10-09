/*******************************************************************************
 * Copyright (C) 2011-2017 Gerd Wuetherich (gerd@gerd-wuetherich.de). All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Gerd Wuetherich (gerd@gerd-wuetherich.de) - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.api.graphdb;

import java.io.File;

/**
 * <p>
 * Factory to create {@link IGraphDb} instances.
 * </p>
 * <p>
 * The creation of a graph database instance requires at least the specification of a port and and store directory. The
 * port parameter specifies the port clients should use to connect against this graph database via the BOLT protocol.
 * </p>
 * <p>
 * Example: <code><pre>
 * IGraphDbFactory graphDbFactory = ...;
 *
 * graphDbFactory.newGraphDb(5001, "/home/exampleUser/databaseDir").create();
 * </pre></code>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IGraphDbFactory {

  /**
   * <p>
   * Creates a new {@link IGraphDbBuilder} to configure a graph database instance.
   * </p>
   *
   * @param databaseDir
   *          the database directory
   * @return a newly created {@link IGraphDbBuilder}
   */
  IGraphDbBuilder newGraphDb(File databaseDir);

  /**
   * <p>
   * Creates a new {@link IGraphDbBuilder} to configure a graph database instance.
   * </p>
   *
   * @param port
   *          clients should use to connect against this graph database via the BOLT protocol.
   * @param databaseDir
   *          the database directory
   * @return a newly created {@link IGraphDbBuilder}
   */
  IGraphDbBuilder newGraphDb(int port, File databaseDir);

  /**
   * <p>
   * The {@link IGraphDbBuilder} is returned by the {@link IGraphDbFactory#newGraphDb(int, File)} method to allow the
   * configuration of the database to create.
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public interface IGraphDbBuilder {

    /**
     * <p>
     * Sets an arbitrary object (the user object) that is associated with the database instance. This user object can be
     * requested from the {@link IGraphDb} via {@link IGraphDb#getUserObject(Class)}.
     * </p>
     *
     * @param userObject
     *          an arbitrary object that is associated with the database instance.
     * @return this {@link IGraphDbBuilder}
     */
    <T> IGraphDbBuilder withUserObject(T userObject);

    /**
     * <p>
     * Sets a configuration value. Which values are allowed here depends on the underlying graph database
     * implementation.
     * </p>
     *
     * @param key
     *          the key of the value.
     * @param value
     *          the value of the configuration item.
     * @return this {@link IGraphDbBuilder}
     */
    IGraphDbBuilder withConfiguration(String key, Object value);

    /**
     * <p>
     * Finally creates the database.
     * </p>
     *
     * @return the created database.
     */
    IGraphDb create();
  }
}
