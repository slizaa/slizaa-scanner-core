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
package org.slizaa.scanner.contentdefinition;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slizaa.scanner.spi.contentdefinition.AbstractContentDefinitionProvider;
import org.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProviderFactory;

public class DirectoryBasedContentDefinitionProvider extends AbstractContentDefinitionProvider<DirectoryBasedContentDefinitionProvider> {

  /** - */
  private List<File> _directoriesWithBinaryArtifacts;

  /** - */
  private final IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> _contentDefinitionProviderFactory;
  
  public boolean add(File e) {
    return _directoriesWithBinaryArtifacts.add(e);
  }

  public boolean addAll(Collection<? extends File> c) {
    return _directoriesWithBinaryArtifacts.addAll(c);
  }
 
  
  @Override
  public IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> getContentDefinitionProviderFactory() {
    return _contentDefinitionProviderFactory;
  }
  
  @Override
  public String toExternalRepresentation() {
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onInitializeProjectContent() {

    // collect dirs
    for (File directories : _directoriesWithBinaryArtifacts) {

      //
      for (File artifact : collectJars(directories)) {

        NameAndVersionInfo info = NameAndVersionInfo.resolveNameAndVersion(artifact);

        if (!info.isSource()) {

          this.createFileBasedContentDefinition(info.getName(), info.getVersion(), new File[] { artifact }, null,
              AnalyzeMode.BINARIES_ONLY);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onDisposeProjectContent() {
    //
  }

  /**
   * <p>
   * </p>
   *
   * @param directory
   * @return
   */
  private List<File> collectJars(File directory) {

    // path
    Path path = directory.toPath();

    // create result
    final List<File> result = new ArrayList<>();

    //
    try {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          if (!attrs.isDirectory()) {
            result.add(file.toFile());
          }
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    //
    return result;
  }
  
  /**
   * <p>
   * Creates a new instance of type {@link DirectoryBasedContentDefinitionProvider}.
   * </p>
   */
  DirectoryBasedContentDefinitionProvider(IContentDefinitionProviderFactory<DirectoryBasedContentDefinitionProvider> contentDefinitionProviderFactory) {
    _directoriesWithBinaryArtifacts = new ArrayList<>();
    _contentDefinitionProviderFactory = checkNotNull(contentDefinitionProviderFactory);
  }

}
