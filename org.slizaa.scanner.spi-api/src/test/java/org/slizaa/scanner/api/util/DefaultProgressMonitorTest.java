package org.slizaa.scanner.api.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class DefaultProgressMonitorTest {

    /**
     *
     */
    @Test
    public void testDefaultProgressMonitor() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 250,
                (progressContext) -> System.out.println(String.format("%s%%", progressContext.getWorkDoneInPercentage())));) {

            //
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(0);

            //
            for (int i = 1; i <= 250; i++) {
                progressMonitor.advance(1);
                assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(i);
            }
        }
    }

    /**
     *
     */
    @Test
    public void testSubMonitor() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 250,
                (progressContext) -> System.out.println(String.format("%s%%", progressContext.getWorkDoneInPercentage())));) {

            IProgressMonitor submonitor = progressMonitor.subTask("Check 1")
                    .withParentConsumptionInPercentage(20)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(50);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(20);

            submonitor = progressMonitor.subTask("Check 2")
                    .withParentConsumptionInPercentage(50)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(175);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(70);

            submonitor = progressMonitor.subTask("Check 3")
                    .withParentConsumptionInPercentage(30)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(250);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(100);

        }
    }

    @Test
    public void testSubMonitor2() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 250,
                (progressContext) -> System.out.println(String.format("%s%%", progressContext.getWorkDoneInPercentage())));) {

            IProgressMonitor submonitor = progressMonitor.subTask("Check 1")
                    .withParentConsumptionInWorkTicks(50)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(50);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(20);

            submonitor = progressMonitor.subTask("Check 2")
                    .withParentConsumptionInWorkTicks(125)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(175);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(70);

            submonitor = progressMonitor.subTask("Check 3")
                    .withParentConsumptionInWorkTicks(75)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(250);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(100);

        }
    }

    @Test
    public void testSubMonitor_3() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 250,
                (progressContext) -> System.out.println(String.format("%s%%", progressContext.getWorkDoneInPercentage())));) {

            // sm 1
            try (IProgressMonitor submonitor = progressMonitor.subTask("Check 1")
                    .withParentConsumptionInWorkTicks(50)
                    .withTotalWorkTicks(100)
                    .create()) {

                consumeSubmonitor(submonitor);
                assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(50);
                assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(20);
            }

            // sm 2
            try (IProgressMonitor submonitor = progressMonitor.subTask("Check 2")
                    .withParentConsumptionInWorkTicks(125)
                    .withTotalWorkTicks(100)
                    .create()) {

                try (IProgressMonitor submonitor_2 = submonitor.subTask("Check 2")
                        .withParentConsumptionInPercentage(100)
                        .withTotalWorkTicks(100)
                        .create()) {

                    consumeSubmonitor(submonitor_2);

                    //
                    ((DefaultProgressMonitor) submonitor_2).dump();
                }

                assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(175);
                assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(70);
            }

            // sm 3
            try (IProgressMonitor submonitor = progressMonitor.subTask("Check 3")
                    .withParentConsumptionInWorkTicks(75)
                    .withTotalWorkTicks(100)
                    .create()) {

                consumeSubmonitor(submonitor);
                assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(250);
                assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(100);

            }
        }
    }

    @Test
    public void testSubMonitor_4() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 100,
                (progressContext) ->
                        System.out.println(String.format("%s%%", progressContext.getWorkDoneInPercentage()))
        );) {

            // sm 1
            try (IProgressMonitor submonitor = progressMonitor.subTask("Check 1")
                    .withParentConsumptionInPercentage(100)
                    .withTotalWorkTicks(2)
                    .create()) {

                try (IProgressMonitor subsubmonitor = submonitor.subTask("Check 2")
                        .withParentConsumptionInPercentage(10)
                        .withTotalWorkTicks(100)
                        .create()) {

                    consumeSubmonitor(subsubmonitor);
                }

                try (IProgressMonitor subsubmonitor = submonitor.subTask("Check 2")
                        .withParentConsumptionInPercentage(90)
                        .withTotalWorkTicks(100)
                        .create()) {

                    consumeSubmonitor(subsubmonitor);
                }
            }
        }
    }

    /**
     * @param progressMonitor
     */
    private void consumeSubmonitor(IProgressMonitor progressMonitor) {

        //
        assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(0);

        //
        for (int i = 1; i <= 100; i++) {
            progressMonitor.advance(1);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(i);
        }

        //
        progressMonitor.close();
    }
}
