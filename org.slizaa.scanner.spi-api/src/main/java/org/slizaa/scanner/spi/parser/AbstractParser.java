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
package org.slizaa.scanner.spi.parser;

import static org.slizaa.scanner.spi.internal.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import org.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import org.slizaa.scanner.spi.contentdefinition.filebased.IFile;
import org.slizaa.scanner.spi.parser.model.INode;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractParser<P extends IParserFactory> implements IParser {

  /** - */
  private P              _parserFactory;

  /** - */
  private List<IProblem> _problems;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractParser}.
   * </p>
   * 
   * @param parserFactory
   */
  public AbstractParser(P parserFactory) {

    //
    checkNotNull(parserFactory);

    //
    _parserFactory = parserFactory;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public P getParserFactory() {
    return _parserFactory;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected List<IProblem> getProblems() {
    return _problems;
  }

  /**
   *
   * @param content
   * @param resource
   *          the resource to parse
   * @param resourceBean
   *          the resource bean that represents the resource that has to be parsed
   * @param context
   * @return
   */
  @Override
  public final List<IProblem> parseResource(IContentDefinition content, IFile resource,
      INode resourceBean, IParserContext context) {

    // Reset problem list
    _problems = new LinkedList<IProblem>();

    // do the parsing
    doParseResource(content, resource, resourceBean, context);

    //
    return _problems;
  }

  /**
   * Override in subclasses to implement parse logic
   * 
   * @param content
   * @param resource
   * @param context
   */
  protected abstract void doParseResource(IContentDefinition content, IFile resource, INode resourceBean,
      IParserContext context);

  /**
   * <p>
   * </p>
   * 
   * @param resource
   * @return
   */
  public abstract boolean canParse(IFile resource);

}
