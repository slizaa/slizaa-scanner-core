package org.slizaa.scanner.api.util;

/**
 *
 */
public interface IProgressMonitor {

  /**
   *
   * @return
   */
  int getWorkDone();

  /**
   *
   * @return
   */
  int getWorkTotal();

  /**
   *
   * @param name
   */
  void subTask(String name);

  /**
   *
   * @param work
   */
  void worked(int work);

  //
  ISubProgressMonitorCreator newChild(String taskname);

  /**
   *
   */
  void done();

  public static interface ISubProgressMonitorCreator {

    ISubProgressMonitorCreator consumeParentTicks(int parentTicks);

    ISubProgressMonitorCreator withTotalWork(int totalWork);

    IProgressMonitor create();
  }
}
