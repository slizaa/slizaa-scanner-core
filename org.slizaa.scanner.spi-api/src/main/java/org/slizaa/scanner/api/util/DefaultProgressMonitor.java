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
    private String _taskName;

    //
    private String _subTaskName;

    //
    private long _workDone;

    //
    private long _totalWork;

    //
    private List<SubMonitor> _subMonitors;

    //
    private boolean _complete;

    /**
     *
     */
    public DefaultProgressMonitor(String name, long totalWork) {
        this(name, totalWork, null);
    }

    /**
     *
     */
    public DefaultProgressMonitor(String name, long totalWork, Consumer<IProgressStatus> progressStatusConsumer) {
        checkNotNull(name);
        checkState(totalWork > 0, "Parameter 'totalWork' must be greater than zero.");

        //
        _taskName = name;
        _totalWork = totalWork;
        _progressStatusConsumer = progressStatusConsumer;

        //
        _subMonitors = new ArrayList<>();
        _workDone = 0;
        _complete = false;
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
    public long getWorkDoneInTicks() {
        return _complete ? _totalWork : _workDone + accumulatedSubMonitorWorkDone();
    }

    @Override
    public long getTotalWorkTicks() {
        return _totalWork;
    }

    @Override
    public int getWorkDoneInPercentage() {
        return (int) (getWorkDoneInTicks() * 100 / _totalWork);
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
        if (_workDone + totalSubMonitorWork + work > _totalWork) {
            System.out.println("ERROR!!");
            _workDone = _totalWork - totalSubMonitorWork;
        }
        //
        else {
            _workDone = _workDone + work;
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
            stringBuilder.append(((SubMonitor) this).getParentTicks());
            stringBuilder.append("/");
            stringBuilder.append(((SubMonitor) this).parent.getTotalWorkTicks());
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
        return _subMonitors.stream()
                .mapToLong(sm -> {
                    return Math.round(((double) sm.getParentTicks() * (double) sm.getWorkDoneInTicks()) / (double) sm.getTotalWorkTicks());
                }).sum();
    }

    private long accumulatedSubMonitorTotalWork() {
        return _subMonitors.stream()
                .mapToLong(sm -> sm.getParentTicks()).sum();
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
        public SubMonitor(String name, long parentTicks, long totalWork, DefaultProgressMonitor parent) {
            super(name, totalWork);

            this.parentTicks = parentTicks;
            this.parent = checkNotNull(parent);

            parent._subMonitors.add(this);
        }

        /**
         * @return
         */
        public long getParentTicks() {
            return parentTicks;
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
        private long _parentWorkTicks = -1;

        //
        private long _totalWork;

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
        public ISubProgressMonitorCreator withParentConsumptionInWorkTicks(long parentWorkTicks) {
            _parentWorkTicks = parentWorkTicks;
            return this;
        }

        @Override
        public ISubProgressMonitorCreator withTotalWorkTicks(long totalWork) {
            _totalWork = totalWork;
            return this;
        }

        @Override
        public IProgressMonitor create() {
            return new SubMonitor(this._name,
                    _parentWorkTicks > 0 ? _parentWorkTicks : (_parentPercentage * _parent._totalWork) / 100, _totalWork,
                    _parent);
        }
    }
}
