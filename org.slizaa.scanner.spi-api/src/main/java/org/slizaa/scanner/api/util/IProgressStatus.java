package org.slizaa.scanner.api.util;

public interface IProgressStatus {

    /**
     * @return
     */
    int getTotalWorkDone();

    /**
     * @return
     */
    int getWorkDoneInPercentage();

    /**
     * @return
     */
    int getTotalWork();

    /**
     * @return
     */
    String getCurrentTask();
}
