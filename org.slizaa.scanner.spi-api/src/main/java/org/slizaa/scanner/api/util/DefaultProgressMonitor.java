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
  public DefaultProgressMonitor(String name, int totalWork) {
    this(name, totalWork, null);
  }

  /**
   *
   */
  public DefaultProgressMonitor(String name, int totalWork, BiConsumer<Integer, Integer> progressConsumer) {
    _taskName = checkNotNull(name);
    checkState(totalWork > 0, "Parameter 'totalWork' must be greater than zero.");
    _subMonitors = new ArrayList<>();
    _workDone = 0;
    _progressConsumer = progressConsumer;
    _totalWork = totalWork;
  }

  /**
   * @param name
   */
  @Override
  public void subTask(String name) {
    _subTaskName = name;
  }

  /**
   * @param work
   */
  @Override
  public void worked(int work) {
    checkState(work > 0, "Parameter work has be greater than 0.");

    _workDone = _workDone + work;

    if (_progressConsumer != null) {
      _progressConsumer.accept(_workDone, _totalWork);
    }

//    int newWorkDone = _workDone + work;
//    int accumulatedSubMonitorWorkDone = accumulatedSubMonitorWorkDone();

//    //
//    if (newWorkDone + accumulatedSubMonitorWorkDone > _totalWork) {
//      LOGGER.error("Work done ({}) is greater than the specified total work ({}).",
//          newWorkDone + accumulatedSubMonitorWorkDone, _totalWork);
//      _workDone = _totalWork;
//    }
//    //
//    else {
//      _workDone = newWorkDone;
//      if (_progressConsumer != null) {
//        _progressConsumer.accept(_workDone, _totalWork);
//      }
//    }
  }

  @Override
  public IProgressMonitor newChildWithParentTicks(String name, int parentTicks, int totalWork) {
    SubMonitor subMonitor = new SubMonitor(name, parentTicks, totalWork, this);
    _subMonitors.add(subMonitor);
    return subMonitor;
  }

  @Override
  public IProgressMonitor newChild(String name, int totalWorkOfChildMonitor) {
    checkState(_subMonitors.isEmpty(), "BUMM");
    SubMonitor subMonitor = new SubMonitor(name, _totalWork, totalWorkOfChildMonitor, this);
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

    /* - */
    private DefaultProgressMonitor parent;

    /**
     * @param parentTicks
     */
    public SubMonitor(String name, int parentTicks,  int totalWork, DefaultProgressMonitor parent) {
      super(name, totalWork);

      this.parentTicks = parentTicks;
      this.parent = checkNotNull(parent);

      _progressConsumer = (a, b) -> System.out.println(
          getRootMonitor().accumulatedSubMonitorWorkDone() + " : " + getRootMonitor()._totalWork);
    }

    /**
     * @return
     */
    public int getParentTicks() {
      return parentTicks;
    }

    public DefaultProgressMonitor getRootMonitor() {
      if (parent instanceof SubMonitor) {
        return ((SubMonitor) parent).getRootMonitor();
      }
      return parent;
    }
  }
}
