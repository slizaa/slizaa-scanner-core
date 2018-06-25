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
package org.slizaa.scanner.core.contentdefinition;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.ops4j.pax.url.mvn.MavenResolvers;
import org.slizaa.scanner.core.spi.contentdefinition.ContentType;
import org.slizaa.scanner.core.spi.contentdefinition.filebased.IFile;
import org.slizaa.scanner.core.spi.internal.contentdefinition.filebased.FileBasedContentDefinition;

public class DefaultContentDefinitionTest {

  @Test
  public void test() throws IOException {

    File resolvedFile = MavenResolvers.createMavenResolver(null, null).resolve("org.mapstruct", "mapstruct-processor",
        null, "jar", "1.2.0.CR2");

    FileBasedContentDefinition contentDefinition = new FileBasedContentDefinition();
    contentDefinition.setName("module_1");
    contentDefinition.setName("1.2.3");
    contentDefinition.addRootPath(resolvedFile, ContentType.BINARY);
    contentDefinition.initialize();

    //
    for (IFile resource : contentDefinition.getBinaryFiles()) {
      System.out.println(resource);
    }
  }

  // @Test
  // public void test2() throws IOException {
  //
  // FileBasedContentDefinitionProvider contentDefinitionProvider = new FileBasedContentDefinitionProvider();
  //
  // //
  // for (IFileBasedContentDefinition contentDefinition : contentDefinitionProvider.getContentDefinitions()) {
  // for (IFile resource : contentDefinition.getBinaryFiles()) {
  // System.out.println(resource);
  // System.out.println(resource.getContent());
  // }
  // }
  // }
}
