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
package org.slizaa.scanner.core.spi.parser.model.resource;

import org.slizaa.scanner.core.spi.parser.model.INode;

/**
 * <p>
 * Defines the interface for a module node bean.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IModuleNode extends INode {

  /**
   * <p>
   * Returns the module version.
   * </p>
   */
  public static final String PROPERTY_MODULE_VERSION   = "version";

  /**
   * <p>
   * Returns the module version.
   * </p>
   */
  public static final String PROPERTY_MODULE_NAME      = INode.NAME;

  /**
   * <p>
   * Returns the module version.
   * </p>
   */
  public static final String PROPERTY_CONTENT_ENTRY_ID = "contentEntryId";
}
