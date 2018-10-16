package org.slizaa.scanner.api.util;

/**
 *
 */
public class NullProgressMonitor implements IProgressMonitor {

    @Override
    public void close() {
        //
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public String getCurrentStep() {
        return null;
    }

    @Override
    public long getWorkDoneInTicks() {
        return 0;
    }

    @Override
    public int getWorkDoneInPercentage() {
        return 0;
    }

    @Override
    public long getTotalWorkTicks() {
        return 0;
    }

    @Override
    public void step(String name) {

    }

    @Override
    public void advance(int work) {

    }

    @Override
    public ISubProgressMonitorCreator subTask(String taskName) {
        return new NullProgressMonitorCreator();
    }

    private static class NullProgressMonitorCreator implements ISubProgressMonitorCreator {
        @Override
        public ISubProgressMonitorCreator withParentConsumptionInPercentage(int percentage) {
            return this;
        }

        @Override
        public ISubProgressMonitorCreator withTotalWorkTicks(long totalWork) {
            return this;
        }

        @Override
        public ISubProgressMonitorCreator withParentConsumptionInWorkTicks(long parentWorkTicks) {
            return this;
        }

        @Override
        public IProgressMonitor create() {
            return new NullProgressMonitor();
        }
    }

}
