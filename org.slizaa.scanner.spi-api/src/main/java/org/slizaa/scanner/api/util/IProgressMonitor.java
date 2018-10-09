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
   * @param totalWork
   */
  void beginTask(String name, int totalWork);

  /**
   *
   * @param work
   */
  void worked(int work);

  /**
   *
   * @param name
   */
  void subTask(String name);

  /**
   *
   * @param subTaskName
   * @param totalWork
   * @return
   */
  IProgressMonitor createSubMonitor(String subTaskName, int totalWork);

  /**
   *
   */
  void done();
}
