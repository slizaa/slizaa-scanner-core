package org.slizaa.scanner.api.util;

public interface IProgressStatus {

    /**
     * @return
     */
    int getWorkDoneInTicks();

    /**
     * @return
     */
    int getWorkDoneInPercentage();

    /**
     * @return
     */
    int getTotalWorkTicks();

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
