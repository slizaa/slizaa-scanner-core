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

import org.ops4j.pax.url.mvn.MavenResolvers;
import org.slizaa.scanner.spi.contentdefinition.AbstractContentDefinitionProvider;
import org.slizaa.scanner.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Superclass for all implementations of {@link ITempDefinitionProvider}
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MvnBasedContentDefinitionProvider extends AbstractContentDefinitionProvider
    implements IContentDefinitionProvider {

  /** - */
  private List<MavenCoordinates> _mavenCoordinates;

  /**
   * <p>
   * Creates a new instance of type {@link MvnBasedContentDefinitionProvider}.
   * </p>
   */
  public MvnBasedContentDefinitionProvider() {
    _mavenCoordinates = new LinkedList<>();
  }

  /**
   * <p>
   * </p>
   *
   * @param groupId
   * @param artifactId
   * @param version
   */
  public void addArtifact(String groupId, String artifactId, String version) {
    _mavenCoordinates.add(new MavenCoordinates(groupId, artifactId, version));
  }

  /**
   */
  protected void onInitializeProjectContent() {

    //
    for (MavenCoordinates mavenCoordinates : _mavenCoordinates) {

      String moduleName = mavenCoordinates.artifactId;
      String moduleVersion = mavenCoordinates.version;

      try {
        File resolvedFile = MavenResolvers.createMavenResolver(null, null).resolve(mavenCoordinates.getGroupId(),
            mavenCoordinates.getArtifactId(), null, "jar", mavenCoordinates.getVersion());

        this.createFileBasedContentDefinition(moduleName, mavenCoordinates.version, new File[] { resolvedFile }, null,
            AnalyzeMode.BINARIES_ONLY);

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
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
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  static class MavenCoordinates {

    private String groupId;

    private String artifactId;

    private String version;

    public MavenCoordinates(String groupId, String artifactId, String version) {
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.version = version;
    }

    public String getGroupId() {
      return groupId;
    }

    public String getArtifactId() {
      return artifactId;
    }

    public String getVersion() {
      return version;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
      result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
      result = prime * result + ((version == null) ? 0 : version.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      MavenCoordinates other = (MavenCoordinates) obj;
      if (artifactId == null) {
        if (other.artifactId != null)
          return false;
      } else if (!artifactId.equals(other.artifactId))
        return false;
      if (groupId == null) {
        if (other.groupId != null)
          return false;
      } else if (!groupId.equals(other.groupId))
        return false;
      if (version == null) {
        if (other.version != null)
          return false;
      } else if (!version.equals(other.version))
        return false;
      return true;
    }
  }
}
