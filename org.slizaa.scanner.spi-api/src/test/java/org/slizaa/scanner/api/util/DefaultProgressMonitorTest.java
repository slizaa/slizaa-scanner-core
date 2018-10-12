package org.slizaa.scanner.api.util;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class DefaultProgressMonitorTest {

  private DefaultProgressMonitor progressMonitor;

  @Before
  public void setup() {
    progressMonitor = new DefaultProgressMonitor("Master check", 100,
        (done, total) -> System.out.println(String.format("%s%%", (done * 100 / total))));
  }

  /**
   *
   */
  @Test
  public void testDefaultProgressMonitor() {

    //
    assertThat(progressMonitor.getWorkDone()).isEqualTo(0);

    //
    for (int i = 1; i <= 100; i++) {
      progressMonitor.worked(1);
      assertThat(progressMonitor.getWorkDone()).isEqualTo(i);
    }

    progressMonitor.done();
  }

  /**
   *
   */
  @Test
  public void testSubMonitor() {

    try {

      IProgressMonitor submonitor = progressMonitor.newChildWithParentTicks("Check 1", 20, 100);
      consumeSubmonitor(submonitor);
      assertThat(progressMonitor.getWorkDone()).isEqualTo(20);

      submonitor = progressMonitor.newChildWithParentTicks("Check 2", 50, 100);
      consumeSubmonitor(submonitor);
      assertThat(progressMonitor.getWorkDone()).isEqualTo(70);

      submonitor = progressMonitor.newChildWithParentTicks("Check 3", 30, 100);
      consumeSubmonitor(submonitor);
      assertThat(progressMonitor.getWorkDone()).isEqualTo(100);

    } finally {
      progressMonitor.done();
    }
  }

//  @Test
//  public void testSubMonitor2() {
//
//    try {
//      progressMonitor.startProgress(100);
//
//      //
//      IProgressMonitor submonitor = progressMonitor.newChild("Check 1", 20);
//      IProgressMonitor subsubmonitor1  = submonitor.newChild("Check 1.1", 40);
//      consumeSubmonitor(subsubmonitor1);
//      IProgressMonitor subsubmonitor2  = submonitor.newChild("Check 1.2", 60);
//      consumeSubmonitor(subsubmonitor2);
//      assertThat(progressMonitor.getWorkDone()).isEqualTo(20);
//
//      //
//      submonitor = progressMonitor.newChild("Check 2", 70);
//      subsubmonitor1  = submonitor.newChild("Check 2.1", 50);
//      consumeSubmonitor(subsubmonitor1);
//      subsubmonitor2  = submonitor.newChild("Check 2.2", 50);
//      consumeSubmonitor(subsubmonitor2);
//      assertThat(progressMonitor.getWorkDone()).isEqualTo(70);
//
//      //
//      submonitor = progressMonitor.newChild("Check 3", 10);
//      subsubmonitor1  = submonitor.newChild("Check 3.1", 60);
//      consumeSubmonitor(subsubmonitor1);
//      subsubmonitor2  = submonitor.newChild("Check 3.2", 40);
//      consumeSubmonitor(subsubmonitor2);
//      assertThat(progressMonitor.getWorkDone()).isEqualTo(100);
//
//    } finally {
//      progressMonitor.done();
//    }
//  }

  /**
   * @param progressMonitor
   */
  private void consumeSubmonitor(IProgressMonitor progressMonitor) {

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
