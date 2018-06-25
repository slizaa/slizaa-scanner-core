/**
 *
 */
package org.slizaa.scanner.core.contentdefinition;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.util.Arrays;

import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ContentDefinitionProviderFactory {

  /**
   * <p>
   * </p>
   *
   * @param file
   * @return
   */
  public static IContentDefinitionProvider fromDirectory(File... files) {

    //
    checkNotNull(files);
    for (File f : files) {
      checkState(f.isDirectory());
    }

    //
    DirectoryBasedContentDefinitionProvider provider = new DirectoryBasedContentDefinitionProvider();
    provider.addAll(Arrays.asList(files));

    //
    return provider;
  }
}
