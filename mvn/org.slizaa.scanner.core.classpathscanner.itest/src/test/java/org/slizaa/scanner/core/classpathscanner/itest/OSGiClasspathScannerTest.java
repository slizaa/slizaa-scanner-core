package org.slizaa.scanner.core.classpathscanner.itest;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.slizaa.scanner.core.classpathscanner.IOSGiBundleClasspathScannerService;

import com.google.common.annotations.Beta;

public class OSGiClasspathScannerTest extends AbstractEclipseTest {

  /** - */
  @Inject
  private IOSGiBundleClasspathScannerService _classpathScannerService;

  @Test
  public void testIt() {
    assertThat(this._classpathScannerService).isNotNull();

    System.out.println(this._classpathScannerService.getExtensionsWithClassAnnotation(Beta.class));
  }
}
