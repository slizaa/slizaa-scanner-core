package org.slizaa.scanner.api.util;

/**
 *
 */
public interface IProgressMonitor extends IProgressStatus {

    /**
     * @param name
     */
    void subTask(String name);

    /**
     * @param work
     */
    void worked(int work);

    /**
     * @param taskName
     * @return
     */
    ISubProgressMonitorCreator newChild(String taskName);

    /**
     *
     */
    void done();

    /**
     *
     */
    public static interface ISubProgressMonitorCreator {

        /**
         * @param percentage
         * @return
         */
        ISubProgressMonitorCreator withParentConsumption(int percentage);

        /**
         * @param totalWork
         * @return
         */
        ISubProgressMonitorCreator withTotalWork(int totalWork);

        /**
         * @return
         */
        IProgressMonitor create();
    }
}
