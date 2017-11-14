package org.slizaa.mojo.copydependencies;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Test;

/**
 */
public class Copy_Maven_Resolver_ExcludeArtifacts extends AbstractCopyDependencyTest {

  /**
   * <p>
   * Creates a new instance of type {@link Copy_Maven_Resolver_ExcludeArtifacts}.
   * </p>
   */
  public Copy_Maven_Resolver_ExcludeArtifacts() {
    super("copy-maven-resolver-excludeArtifacts");
  }

  @Test
  public void testCopyDependencyMojo() throws Exception {

    //
    CopyDependenciesMojo mojo = (CopyDependenciesMojo) super.findCopyDependencyMojo("copyDependencies");
    mojo.execute();

    //
    List<File> copiedFiles = getCopiedFiles();
    assertThat(copiedFiles).hasSize(58);
  }
}