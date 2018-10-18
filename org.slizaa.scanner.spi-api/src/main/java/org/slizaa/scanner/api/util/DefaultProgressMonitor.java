package org.slizaa.scanner.api.util;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.slizaa.scanner.spi.internal.Preconditions.checkNotNull;
import static org.slizaa.scanner.spi.internal.Preconditions.checkState;

/**
 *
 */
public class DefaultProgressMonitor implements IProgressMonitor {

  //
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProgressMonitor.class);

  //
  protected Consumer<IProgressStatus> _progressStatusConsumer;

  //
  protected String _taskName;

  //
  protected String _subTaskName;

  //
  protected long _workDone;

  //
  protected long _totalWork;

  //
  protected List<SubMonitor> _subMonitors;

  //
  protected boolean _complete;

  /**
   *
   */
  public DefaultProgressMonitor(String name, int totalWork) {
    this(name, totalWork, null);
  }

  /**
   *
   */
  public DefaultProgressMonitor(String name, int totalWork, Consumer<IProgressStatus> progressStatusConsumer) {
    checkNotNull(name);
    checkState(totalWork > 0, "Parameter 'totalWork' must be greater than zero.");

    //
    _taskName = name;
    _totalWork = scaleUp(totalWork);
    _progressStatusConsumer = progressStatusConsumer;

    //
    _subMonitors = new ArrayList<>();
    _workDone = 0;
    _complete = false;
  }

  /**
   * @return
   */
  public static Consumer<IProgressStatus> consoleLogger() {
    return new ConsoleLogger();
  }

  private static int scaleDown(long value) {
    return (int) (value / 1000L);
  }

  private static long scaleUp(int value) {
    return value * 1000L;
  }

  @Override
  public String getCurrentStep() {
    // TODO
    return null;
  }

  @Override
  public boolean isComplete() {
    return _complete;
  }

  @Override
  public int getWorkDoneInTicks() {
    return scaleDown(internalGetWorkDoneInTicks());
  }

  @Override
  public int getTotalWorkTicks() {
    return scaleDown(internalGetTotalWorkTicks());
  }

  @Override
  public int getWorkDoneInPercentage() {
    return (int) (((float) internalGetWorkDoneInTicks() / (float) _totalWork) * 100);
  }

  protected long internalGetWorkDoneInTicks() {
    return _complete ? _totalWork : _workDone + accumulatedSubMonitorWorkDone();
  }

  protected long internalGetTotalWorkTicks() {
    return _totalWork;
  }

  /**
   * @param name
   */
  @Override
  public void step(String name) {

    //
    if (_complete) {
      LOGGER.warn("Calling 'step({}) on a completed progress monitor.'");
      return;
    }

    //
    _subTaskName = name;
  }

  /**
   * @param work
   */
  @Override
  public void advance(int work) {
    checkState(work >= 0, "Parameter work has be greater than or equal 0.");

    //
    if (_complete) {
      return;
    }

    //
    if (!_subMonitors.isEmpty()) {
      SubMonitor lastActiveSubMonitor = _subMonitors.get(_subMonitors.size() - 1);
      if (!lastActiveSubMonitor.isComplete()) {
        lastActiveSubMonitor.close();
      }
    }

    //
    long totalSubMonitorWork = accumulatedSubMonitorTotalWork();
    if (_workDone + totalSubMonitorWork + scaleUp(work) > _totalWork) {
      System.out.println("ERROR!!");
      _workDone = _totalWork - totalSubMonitorWork;
    }
    //
    else {
      _workDone = _workDone + scaleUp(work);
    }

    //
    fireProgressStatus();
  }

  /**
   * @param taskName
   * @return
   */
  @Override
  public ISubProgressMonitorCreator subTask(String taskName) {

    //
    checkState(!_complete,
        "Can not create a new sub task because the parent progress monitor already has been completed.");

    //
    return new DefaultSubProgressMonitorCreator(taskName, this);
  }

  /**
   *
   */
  @Override
  public void close() {
    _workDone = _totalWork;
    _subTaskName = null;
    _complete = true;

    //
    fireProgressStatus();
  }

  /**
   *
   */
  public void dump() {
    System.out.println(dump(0));
  }

  /**
   * @param indent
   */
  protected String dump(int indent) {

    //
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Strings.repeat("  ", indent));
    stringBuilder.append(this.getClass().getSimpleName());
    stringBuilder.append(": Work Done: ");
    stringBuilder.append(getWorkDoneInTicks());
    stringBuilder.append(", Work Total: ");
    stringBuilder.append(getTotalWorkTicks());
    stringBuilder.append(", WorkDoneInPercentage: ");
    stringBuilder.append(getWorkDoneInPercentage());
    if (this instanceof SubMonitor) {
      stringBuilder.append(", Parent Ticks: ");
      stringBuilder.append(((SubMonitor) this).parentTicks);
      stringBuilder.append("/");
      stringBuilder.append(((SubMonitor) this).parent._totalWork);
    }

    for (SubMonitor subMonitor : _subMonitors) {
      stringBuilder.append("\n");
      stringBuilder.append(subMonitor.dump(indent + 1));
    }

    //
    return stringBuilder.toString();
  }

  /**
   * @return
   */
  protected DefaultProgressMonitor getRootMonitor() {
    return this;
  }

  /**
   * @return
   */
  private long accumulatedSubMonitorWorkDone() {
    return (long) _subMonitors.stream()
        .mapToDouble(sm -> {
          double workDone =
              ((double) sm.parentTicks * (double) sm.internalGetWorkDoneInTicks()) / (double) sm._totalWork;
          return workDone;
        }).sum();
  }

  private long accumulatedSubMonitorTotalWork() {
    return _subMonitors.stream()
        .mapToLong(sm -> sm.parentTicks).sum();
  }

  /**
   *
   */
  private void fireProgressStatus() {

    //
    DefaultProgressMonitor rootMonitor = getRootMonitor();

    if (rootMonitor._progressStatusConsumer != null) {
      rootMonitor._progressStatusConsumer.accept(rootMonitor);
    }
  }

  /**
   * The console logger
   */
  private static class ConsoleLogger implements Consumer<IProgressStatus> {

    //
    private int _lastLoggedPercentage = -1;

    @Override
    public void accept(IProgressStatus progressStatus) {
      int workDoneInPercentage = progressStatus.getWorkDoneInPercentage();
      if (_lastLoggedPercentage != workDoneInPercentage) {
        _lastLoggedPercentage = workDoneInPercentage;
        System.out.println(String.format("%s%%", _lastLoggedPercentage));
      }
    }
  }

  /**
   *
   */
  private class SubMonitor extends DefaultProgressMonitor {

    /* - */
    private long parentTicks;

    /* - */
    private DefaultProgressMonitor parent;

    /**
     * @param parentTicks
     */
    public SubMonitor(String name, long parentTicks, int totalWork, DefaultProgressMonitor parent) {
      super(name, totalWork);

      //
      this.parentTicks = parentTicks;
      this.parent = checkNotNull(parent);

      //
      parent._subMonitors.add(this);
    }

    public void dump() {
      getRootMonitor().dump();
    }

    protected DefaultProgressMonitor getRootMonitor() {
      if (parent instanceof SubMonitor) {
        return ((SubMonitor) parent).getRootMonitor();
      }
      return parent;
    }
  }

  /**
   *
   */
  private class DefaultSubProgressMonitorCreator implements ISubProgressMonitorCreator {

    //
    private String _name;

    //
    private DefaultProgressMonitor _parent;

    //
    private int _parentPercentage;

    //
    private int _parentWorkTicks = -1;

    //
    private int _totalWork;

    /**
     * @param name
     */
    public DefaultSubProgressMonitorCreator(String name, DefaultProgressMonitor parent) {
      this._name = checkNotNull(name);
      this._parent = parent;
    }

    @Override
    public ISubProgressMonitorCreator withParentConsumptionInPercentage(int percentage) {
      _parentPercentage = percentage;
      return this;
    }

    @Override
    public ISubProgressMonitorCreator withParentConsumptionInWorkTicks(int parentWorkTicks) {
      _parentWorkTicks = parentWorkTicks;
      return this;
    }

    @Override
    public ISubProgressMonitorCreator withTotalWorkTicks(int totalWork) {
      _totalWork = totalWork;
      return this;
    }

    @Override
    public IProgressMonitor create() {
      long parentWorkTicks = _parentWorkTicks > 0 ?
          scaleUp(_parentWorkTicks) :
          (long) ((double) _parentPercentage * (double) _parent._totalWork / 100.0);
      return new SubMonitor(this._name, parentWorkTicks, _totalWork, _parent);
    }
  }
}
