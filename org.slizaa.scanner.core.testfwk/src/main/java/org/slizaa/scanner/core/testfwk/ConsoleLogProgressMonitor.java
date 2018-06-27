package org.slizaa.scanner.core.testfwk;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * <p>
 * </p>
 */
public class ConsoleLogProgressMonitor implements IProgressMonitor {

  /** - */
  private double _totalWork;

  /** - */
  private double _worked;

  @Override
  public void beginTask(String name, int totalWork) {
    log("beginTask(%s %s)", name, totalWork);
    this._totalWork = totalWork;
  }

  @Override
  public void done() {
    log("done()");
  }

  @Override
  public void internalWorked(double work) {
    log("internalWorked(%s)", work);
  }

  @Override
  public boolean isCanceled() {
    return false;
  }

  @Override
  public void setCanceled(boolean value) {
  }

  @Override
  public void setTaskName(String name) {
    log("setTaskName()");
  }

  @Override
  public void subTask(String name) {
    log("subTask(%s)", name);
  }

  @Override
  public void worked(int work) {
    // logger.info("worked({})", work);
    this._worked = this._worked + work;
    double r = (this._worked / this._totalWork) * 100;
    log("%s%%", r);
  }

  /**
   * <p>
   * </p>
   *
   * @param string
   * @param r
   */
  private void log(String string, Object... params) {
    System.out.println(String.format(string, params));
  }
}