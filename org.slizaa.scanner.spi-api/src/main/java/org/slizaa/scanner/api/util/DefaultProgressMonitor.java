package org.slizaa.scanner.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static org.slizaa.scanner.spi.internal.Preconditions.checkNotNull;
import static org.slizaa.scanner.spi.internal.Preconditions.checkState;

/**
 *
 */
public class DefaultProgressMonitor implements IProgressMonitor {

  //
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProgressMonitor.class);

  //
  protected BiConsumer<Integer, Integer> _progressConsumer;

  //
  private String _taskName;

  //
  private String _subTaskName;

  //
  private int _workDone;

  //
  private int _totalWork;

  //
  private List<SubMonitor> _subMonitors;

  /**
   *
   */
  public DefaultProgressMonitor() {
    this(null);
  }

  /**
   *
   */
  public DefaultProgressMonitor(BiConsumer<Integer, Integer> progressConsumer) {
    _subMonitors = new ArrayList<>();
    _workDone = 0;
    _progressConsumer = progressConsumer;
  }

  /**
   * @param name
   * @param totalWork
   */
  @Override
  public void beginTask(String name, int totalWork) {
    _taskName = checkNotNull(name);
    _totalWork = totalWork;
  }

  /**
   * @param work
   */
  @Override
  public void worked(int work) {
    checkState(work > 0, "Parameter work has be greater than 0.");

    int newWorkDone = _workDone + work;
    int accumulatedSubMonitorWorkDone = accumulatedSubMonitorWorkDone();

    //
    if (newWorkDone + accumulatedSubMonitorWorkDone > _totalWork) {
      LOGGER.error("Work done ({}) is greater than the specified total work ({}).",
          newWorkDone + accumulatedSubMonitorWorkDone, _totalWork);
      _workDone = _totalWork;
    }
    //
    else {
      _workDone = newWorkDone;
      if (_progressConsumer != null) {
        _progressConsumer.accept(_workDone, _totalWork);
      }
    }
  }

  /**
   * @param name
   */
  @Override
  public void subTask(String name) {
    _subTaskName = name;
  }

  @Override
  public IProgressMonitor createSubMonitor(String subTaskName, int totalWork) {
    SubMonitor subMonitor = new SubMonitor(totalWork);
    _subMonitors.add(subMonitor);
    return subMonitor;
  }

  /**
   *
   */
  @Override
  public void done() {
    _workDone = _totalWork;
    _subTaskName = null;
  }

  @Override
  public int getWorkDone() {
    return _workDone + accumulatedSubMonitorWorkDone();
  }

  @Override
  public int getWorkTotal() {
    return _totalWork;
  }

  /**
   * @return
   */
  private int accumulatedSubMonitorWorkDone() {
    return _subMonitors.stream().mapToInt(sm -> (sm.getParentTicks() * sm.getWorkDone()) / sm.getWorkTotal()).sum();
  }

  /**
   *
   */
  private class SubMonitor extends DefaultProgressMonitor {

    /* - */
    private int parentTicks;

    /**
     * @param parentTicks
     */
    public SubMonitor(int parentTicks) {
      this.parentTicks = parentTicks;

      _progressConsumer = (a, b) -> System.out.println(
          DefaultProgressMonitor.this.accumulatedSubMonitorWorkDone() + " : " + DefaultProgressMonitor.this._totalWork);
    }

    /**
     * @return
     */
    public int getParentTicks() {
      return parentTicks;
    }
  }
}
