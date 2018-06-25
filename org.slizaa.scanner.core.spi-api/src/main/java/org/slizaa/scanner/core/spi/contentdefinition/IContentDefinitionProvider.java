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
package org.slizaa.scanner.core.spi.contentdefinition;

import java.util.List;

/**
 * <p>
 * Represents the definition of the system that should be analyzed. A {@link IContentDefinitionProvider} can be created
 * using the {@link ISystemDefinitionFactory}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IContentDefinitionProvider {

  /**
   * <p>
   * Returns a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
   * </p>
   * 
   * @return a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
   */
  List<? extends IContentDefinition> getContentDefinitions();
}
