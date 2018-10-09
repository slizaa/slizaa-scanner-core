/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.testfwk;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;

import org.slizaa.scanner.contentdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

public class ContentDefinitionProviderFactory {

  /**
   * <p>
   * </p>
   *
   * @param coordinates
   * @return
   */
  public static IContentDefinitionProvider multipleBinaryMvnArtifacts(String[]... coordinates) {

    //
    checkNotNull(coordinates);
    for (String[] c : coordinates) {
      checkState(c.length == 3, "Coordinates must have three parts (groupId, artifactId, version)");
    }

    //
    MvnBasedContentDefinitionProvider provider = new MvnBasedContentDefinitionProvider();

    //
    for (String[] coordinate : coordinates) {
      provider.addArtifact(checkNotNull(coordinate[0]), checkNotNull(coordinate[1]), checkNotNull(coordinate[2]));
    }

    //
    return provider;
  }

  /**
   * <p>
   * </p>
   *
   * @param groupId
   * @param artifactId
   * @param version
   * @return
   */
  public static IContentDefinitionProvider simpleBinaryMvnArtifact(String groupId, String artifactId, String version) {

    return multipleBinaryMvnArtifacts(new String[] { groupId, artifactId, version });
  }

  /**
   * <p>
   * </p>
   *
   * @param name
   * @param version
   * @param jarOrDirectory
   * @return
   */
  public static IContentDefinitionProvider simpleBinaryFile(String name, String version, String jarOrDirectory) {
    return simpleBinaryFile(name, version, new File(jarOrDirectory));
  }

  /**
   * <p>
   * </p>
   *
   * @param name
   * @param version
   * @param jarOrDirectory
   * @return
   */
  public static IContentDefinitionProvider simpleBinaryFile(String name, String version, File jarOrDirectory) {

    checkNotNull(name);
    checkNotNull(version);
    checkNotNull(jarOrDirectory);

    //
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider();

    //
    provider.createFileBasedContentDefinition(name, version, new File[] { jarOrDirectory }, null,
        AnalyzeMode.BINARIES_ONLY);

    //
    return provider;
  }
}
