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
