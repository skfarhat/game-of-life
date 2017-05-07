package gui;

import gui.controllers.RootController;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sami on 06/04/2017.
 */
public class RootControllerTest {
    @Test
    public void calculateFrequencyFromPeriod() throws Exception {

        final double DELTA = 0.000001;

        // 1000msec = 1sec = 1HZ
        int period1 = 1000;
        double expectedFreq1 = 1;

        int period2 = 10000;
        double expectedFreq2 = 0.1;

        int period3 = 100;
        double expectedFreq3 = 10.0;

        assertEquals(expectedFreq1, RootController.calculateFrequencyFromPeriod(period1), DELTA);
        assertEquals(expectedFreq2, RootController.calculateFrequencyFromPeriod(period2), DELTA);
        assertEquals(expectedFreq3, RootController.calculateFrequencyFromPeriod(period3), DELTA);
    }

    @Test
    public void calculatePeriodFromFrequency() throws Exception {
        double freq1 = 1; // 1Hz <--> 1000msec
        int expectedPeriod1 = 1000; // msec

        double freq2 = 0.1; // 0.1Hz <--> 10000msec
        int expectedPeriod2 = 10000;

        double freq3 = 10.0;
        int expectedPeriod3 = 100;

        assertEquals(expectedPeriod1, RootController.calculatePeriodFromFrequency(freq1));
        assertEquals(expectedPeriod2, RootController.calculatePeriodFromFrequency(freq2));
        assertEquals(expectedPeriod3, RootController.calculatePeriodFromFrequency(freq3));
    }


    @Test
    public void testSetFrequency() {
        RootController controller = new RootController();

        controller.setFrequency(0.5);
    }


}