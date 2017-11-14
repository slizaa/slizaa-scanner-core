package org.slizaa.mojo.copydependencies;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Test;

/**
 */
public class Copy_Maven_Resolver extends AbstractCopyDependencyTest {

  /**
   * <p>
   * Creates a new instance of type {@link Copy_Maven_Resolver}.
   * </p>
   */
  public Copy_Maven_Resolver() {
    super("copy-maven-resolver");
  }

  @Test
  public void testCopyDependencyMojo() throws Exception {

    //
    CopyDependenciesMojo mojo = (CopyDependenciesMojo) super.findCopyDependencyMojo("copyDependencies");
    mojo.execute();

    List<File> copiedFiles = getCopiedFiles();
    assertThat(copiedFiles).hasSize(60);
  }
}