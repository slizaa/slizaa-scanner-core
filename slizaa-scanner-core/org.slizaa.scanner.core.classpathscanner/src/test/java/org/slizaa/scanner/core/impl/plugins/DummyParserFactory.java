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
package org.slizaa.scanner.core.impl.plugins;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinition;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.parser.IParser;
import org.slizaa.scanner.core.spi.parser.IParserFactory;

@SlizaaParserFactory
public class DummyParserFactory implements IParserFactory {

  @Override
  public void initialize() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public IParser createParser(IContentDefinitionProvider systemDefinition) {
    return null;
  }

  @Override
  public void batchParseStart(IContentDefinitionProvider systemDefinition, Object graphDatabase, IProgressMonitor subMonitor)
      throws Exception {
  }

  @Override
  public void batchParseStop(IContentDefinitionProvider systemDefinition, Object graphDatabase, IProgressMonitor subMonitor)
      throws Exception {
  }

  @Override
  public void batchParseStartContentDefinition(IContentDefinition contentDefinition) throws Exception {
  }

  @Override
  public void batchParseStopContentDefinition(IContentDefinition contentDefinition) throws Exception {
  }

  @Override
  public void beforeDeleteResourceNode(Object node) {
  }
}
