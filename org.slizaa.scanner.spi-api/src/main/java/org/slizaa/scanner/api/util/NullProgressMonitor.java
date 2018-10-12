package org.slizaa.scanner.api.util;

/**
 *
 */
public class NullProgressMonitor implements IProgressMonitor {

  @Override
  public int getWorkDone() {
    return 0;
  }

  @Override
  public int getWorkTotal() {
    return 0;
  }

  @Override
  public void subTask(String name) {

  }

  @Override
  public void worked(int work) {

  }

  @Override
  public IProgressMonitor newChildWithParentTicks(String name, int parentTicks, int totalWork) {
    return null;
  }

  @Override
  public IProgressMonitor newChild(String name, int totalWork) {
    return null;
  }

  @Override
  public void done() {

  }
}
