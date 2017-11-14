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
package org.slizaa.scanner.core.spi.parser;

import java.util.List;

import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinition;
import org.slizaa.scanner.core.spi.contentdefinition.filebased.IFile;
import org.slizaa.scanner.core.spi.parser.model.INode;

/**
 * <p>
 * Defines the common interface to parse a {@link IParsableResource}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IParser {

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public ParserType getParserType();

  /**
   * <p>
   * </p>
   * 
   * @param resource
   * @return
   */
  boolean canParse(IFile resource);

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   *          the content definition that specifies this resource.
   * @param resource
   *          the resource to parse
   * @param resourceBean
   *          the resource bean that represents the resource that has to be parsed
   * @param parserContext
   *          the parser context
   * @return
   */
  List<IProblem> parseResource(IContentDefinition contentDefinition, IFile resource, INode resourceBean,
      IParserContext parserContext);
}
