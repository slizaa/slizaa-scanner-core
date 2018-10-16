package org.slizaa.scanner.api.util;

public interface IProgressStatus {

    /**
     * @return
     */
    long getWorkDoneInTicks();

    /**
     * @return
     */
    int getWorkDoneInPercentage();

    /**
     * @return
     */
    long getTotalWorkTicks();

    /**
     *
     * @return
     */
    boolean isComplete();

    /**
     * @return
     */
    String getCurrentStep();
}
