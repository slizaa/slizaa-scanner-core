package org.slizaa.scanner.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
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
    public DefaultProgressMonitor(String name, int totalWork, Consumer<IProgressStatus> progressStatusConsumer) {
        _taskName = checkNotNull(name);
        checkState(totalWork > 0, "Parameter 'totalWork' must be greater than zero.");
        _subMonitors = new ArrayList<>();
        _workDone = 0;
        _progressStatusConsumer = progressStatusConsumer;
        _totalWork = totalWork;
    }

    @Override
    public int getWorkDoneInPercentage() {
        return getTotalWorkDone() * 100 / _totalWork;
    }

    /**
     * @param name
     */
    @Override
    public void subTask(String name) {
        _subTaskName = name;
    }

    @Override
    public String getCurrentTask() {
        return null;
    }

    /**
     * @param work
     */
    @Override
    public void worked(int work) {
        checkState(work > 0, "Parameter work has be greater than 0.");

        _workDone = _workDone + work;

        fireProgressStatus();
    }



    protected DefaultProgressMonitor getRootMonitor() {
        return this;
    }

    @Override
    public ISubProgressMonitorCreator newChild(String taskName) {
        return new DefaultSubProgressMonitorCreator(taskName, this);
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
    public int getTotalWorkDone() {
        return _workDone + accumulatedSubMonitorWorkDone();
    }

    @Override
    public int getTotalWork() {
        return _totalWork;
    }

    /**
     * @return
     */
    private int accumulatedSubMonitorWorkDone() {
        return _subMonitors.stream().mapToInt(sm -> (sm.getParentTicks() * sm.getTotalWorkDone()) / sm.getTotalWork()).sum();
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
        private int parentTicks;

        /* - */
        private DefaultProgressMonitor parent;

        /**
         * @param parentTicks
         */
        public SubMonitor(String name, int parentTicks, int totalWork, DefaultProgressMonitor parent) {
            super(name, totalWork);

            this.parentTicks = parentTicks;
            this.parent = checkNotNull(parent);

            parent._subMonitors.add(this);
        }

        /**
         * @return
         */
        public int getParentTicks() {
            return parentTicks;
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
        private int _totalWork;

        /**
         * @param name
         */
        public DefaultSubProgressMonitorCreator(String name, DefaultProgressMonitor parent) {
            this._name = checkNotNull(name);
            this._parent = parent;
        }

        @Override
        public ISubProgressMonitorCreator withParentConsumption(int percentage) {
            _parentPercentage = percentage;
            return this;
        }

        @Override
        public ISubProgressMonitorCreator withTotalWork(int totalWork) {
            _totalWork = totalWork;
            return this;
        }

        @Override
        public IProgressMonitor create() {
            return new SubMonitor(this._name, (_parentPercentage * _parent._totalWork) / 100, _totalWork, _parent);
        }
    }
}
