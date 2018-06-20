package org.slizaa.mojos.ecoregenerator;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.maven.plugin.logging.Log;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.Diagnostic;

/**
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class MonitorToMavenLogAdapter extends BasicMonitor {

  /** - */
  protected Log _mavenLog;

  public MonitorToMavenLogAdapter(Log mavenLog) {
    this._mavenLog = checkNotNull(mavenLog);
  }

  @Override
  public void beginTask(String name, int totalWork) {
    if (name != null && name.length() != 0) {
      this._mavenLog.info(">>> " + name);
    }
  }

  @Override
  public void setTaskName(String name) {
    if (name != null && name.length() != 0) {
      this._mavenLog.info("<>> " + name);
    }
  }

  @Override
  public void subTask(String name) {
    if (name != null && name.length() != 0) {
      this._mavenLog.info(">>  " + name);
    }
  }

  @Override
  public void setBlocked(Diagnostic reason) {
    super.setBlocked(reason);
    this._mavenLog.info("#>  " + reason.getMessage());
  }

  @Override
  public void clearBlocked() {
    this._mavenLog.info("=>  " + getBlockedReason().getMessage());
    super.clearBlocked();
  }
}