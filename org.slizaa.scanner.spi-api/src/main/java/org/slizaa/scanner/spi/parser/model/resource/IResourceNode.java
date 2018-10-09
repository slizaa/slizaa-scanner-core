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
package org.slizaa.scanner.spi.parser.model.resource;

import org.slizaa.scanner.spi.parser.model.INode;

/**
 * <p>
 * Defines the interface for a resource node bean.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IResourceNode extends INode {
  
  /**
   * <p>
   * The full path of the resource, e.g. <code>'org/example/Test.java'</code>. Note that resource paths are always
   * slash-delimited ('/').
   * </p>
   */
  public static final String PROPERTY_PATH               = "path";

  /**
   * <p>
   * the root directory or archive file that contains the resource (e.g. <code>'c:/dev/classes.zip'</code> or
   * <code>'c:/dev/source'</code>). Note that resource paths are always slash-delimited ('/').
   * </p>
   */
  public static final String PROPERTY_ROOT               = "root";


  /**
   * <p>
   * Return <code>true</code> if references of the underlying resource should be parsed.
   * </p>
   */
  public static final String PROPERTY_ANALYSE_REFERENCES = "analyseReferences";

  /**
   * <p>
   * Return <code>true</code> if the last attempt to parse the resource returned one or more errors.
   * </p>
   */
  public static final String PROPERTY_ERRONEOUS          = "erroneous";

  /**
   * <p>
   * Returns the type of the resource (either {@link ResourceType#SOURCE} or {@link ResourceType#BINARY}.
   * </p>
   * 
   * @return the type of the resource (either {@link ResourceType#SOURCE} or {@link ResourceType#BINARY}.
   */
  ResourceType getResourceType();
}
