package org.slizaa.scanner.api.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultProgressMonitorTest {

  @Test
  public void testDefaultProgressMonitor() {

    //
    DefaultProgressMonitor progressMonitor = new DefaultProgressMonitor();
    progressMonitor.beginTask("Test", 100);

    //
    assertThat(progressMonitor.getWorkDone()).isEqualTo(0);

    //
    for (int i = 1; i <= 100; i++) {
      progressMonitor.worked(1);
      assertThat(progressMonitor.getWorkDone()).isEqualTo(i);
    }

    progressMonitor.done();
  }

  @Test
  public void testSubMonitor() {

    //
    DefaultProgressMonitor progressMonitor = new DefaultProgressMonitor(
        (done, total) -> System.out.println(String.format("%s%%", (done / total) * 100)));

    try {
      progressMonitor.beginTask("Archiving internet", 100);

      IProgressMonitor submonitor = progressMonitor.createSubMonitor("downloadInternet", 20);
      consumeSubmonitor(submonitor);
      assertThat(progressMonitor.getWorkDone()).isEqualTo(20);

      submonitor = progressMonitor.createSubMonitor("zipDownloadedInternet", 50);
      consumeSubmonitor(submonitor);
      assertThat(progressMonitor.getWorkDone()).isEqualTo(70);

      submonitor = progressMonitor.createSubMonitor("copyInternetZip", 30);
      consumeSubmonitor(submonitor);
      assertThat(progressMonitor.getWorkDone()).isEqualTo(100);

    } finally {
      progressMonitor.done();
    }
  }

  private void consumeSubmonitor(IProgressMonitor progressMonitor) {

    progressMonitor.beginTask("task 1", 100);
    assertThat(progressMonitor.getWorkDone()).isEqualTo(0);

    //
    for (int i = 1; i <= 100; i++) {
      progressMonitor.worked(1);
      assertThat(progressMonitor.getWorkDone()).isEqualTo(i);
    }

    //
    progressMonitor.done();
  }
}
