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
package org.slizaa.scanner.spi.contentdefinition;

public interface IContentDefinitionProviderFactory<T extends IContentDefinitionProvider> {

  /**
   * 
   * @return
   */
  String getFactoryId();
  
  String getName();
  
  /**
   * 
   * @return
   */
  String getDescription();
  
  T emptyContentDefinitionProvider();
  
  String toExternalRepresentation(T contentDefinitionProvider);
  
  T fromExternalRepresentation(String externalRepresentation);
}
