package org.slizaa.scanner.core.spi.parser;

import org.slizaa.scanner.core.spi.parser.model.INode;

public interface IParserContext {

  boolean parseReferences();

  INode getParentDirectoryNode();

  INode getParentModuleNode();
}
