package org.slizaa.scanner.api.util;

/**
 *
 */
public class NullProgressMonitor implements IProgressMonitor {

    @Override
    public String getCurrentTask() {
        return null;
    }

    @Override
    public int getTotalWorkDone() {
        return 0;
    }

    @Override
    public int getWorkDoneInPercentage() {
        return 0;
    }

    @Override
    public int getTotalWork() {
        return 0;
    }

    @Override
    public void subTask(String name) {

    }

    @Override
    public void worked(int work) {

    }

    @Override
    public ISubProgressMonitorCreator newChild(String taskName) {
        return null;
    }

    @Override
    public void done() {

    }

    private static class NullProgressMonitorCreator implements ISubProgressMonitorCreator {
        @Override
        public ISubProgressMonitorCreator withParentConsumption(int percentage) {
            return this;
        }

        @Override
        public ISubProgressMonitorCreator withTotalWork(int totalWork) {
            return this;
        }

        @Override
        public IProgressMonitor create() {
            return new NullProgressMonitor();
        }
    }

}
