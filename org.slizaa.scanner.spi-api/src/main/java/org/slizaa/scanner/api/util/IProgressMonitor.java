package org.slizaa.scanner.api.util;

/**
 *
 */
public interface IProgressMonitor extends IProgressStatus, AutoCloseable {

    /**
     * @param name
     */
    void step(String name);

    /**
     * @param work
     */
    void advance(int work);

    /**
     * @param taskName
     * @return
     */
    ISubProgressMonitorCreator subTask(String taskName);

    /**
     *
     */
    void close();

    /**
     *
     */
    public static interface ISubProgressMonitorCreator {

        /**
         * @param percentage
         * @return
         */
        ISubProgressMonitorCreator withParentConsumptionInPercentage(int percentage);

      /**
       * @param parentWorkTicks
       * @return
       */
      ISubProgressMonitorCreator withParentConsumptionInWorkTicks(int parentWorkTicks);

        /**
         * @param totalWork
         * @return
         */
        ISubProgressMonitorCreator withTotalWorkTicks(int totalWork);

        /**
         * @return
         */
        IProgressMonitor create();
    }
}
