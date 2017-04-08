package gui;

import core.AgentIsDeadException;
import core.InvalidPositionException;
import core.Life;
import core.Utils;
import core.actions.Action;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @class RootController
 *
 * handles only one life at a time
 */
public class RootController implements Initializable, LifeStarter {

    // ===========================================================================================
    // CONSTANTS
    // ===========================================================================================

    /** @brief period between calls to step() in milli-seconds */
    private final static int DEFAULT_PERIOD = 100;  // 100 milliseconds
    private final static int MIN_PERIOD = 10;       // 10 milliseconds
    private final static int MAX_PERIOD = 200;

    // calculate the frequency ranges fom the (min, max) period
    private static final double MIN_FREQUENCY = calculateFrequencyFromPeriod(MAX_PERIOD);
    private static final double MAX_FREQUENCY = calculateFrequencyFromPeriod(MIN_PERIOD);

    // ===========================================================================================
    // MEMBER VARIABLES
    // ===========================================================================================

    private int period = DEFAULT_PERIOD; // milliseconds
    private Timer timer;
//    private Timeline drawTimer;
    private State currentState = State.STOPPED;
    private Life life;
    private ConcurrentLinkedQueue<Action> queue = new ConcurrentLinkedQueue<>();

    // ===========================================================================================
    // FXML
    // ===========================================================================================

    @FXML private ScrollPane controlPane;
    @FXML private ControlPanelController controlPaneController;

    @FXML private Pane lifeView;
    @FXML private LifeViewController lifeViewController;

    // ===========================================================================================
    // STATIC METHODS
    // ===========================================================================================

    /**
     * @param period in milliseconds
     * @return
     */
    public static double calculateFrequencyFromPeriod(int period) {
        return 1000.0f / period;
    }

    /** @return period in milliseconds from the frequency */
    public static int calculatePeriodFromFrequency(double freq) {
        return (int) (1000.0f / freq);
    }

    // ===========================================================================================
    // METHODS
    // ===========================================================================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlPaneController.setLifeStarter(this);
        lifeViewController.setRootPane(lifeView);
    }

    // TODO(sami): throw appropriate exception here
    public boolean start(Life life) {

        // guard
        if (getState() == State.STARTED) {
            return false;
        }

        this.life = life;

        // set life in the controller
        try { lifeViewController.setLife(life); }
        catch (InvalidPositionException e) { e.printStackTrace(); return false; }

        // run the timers and change state
        runTimers();
        currentState = State.STARTED;

        return true;
    }

    // TODO(sami): create an excpetion for this
    // TODO(sami): how to safely deschedule timers
    @Override
    public boolean stop() {

        // guard
        if (getState() == State.STOPPED) {
            return false;
        }

        this.life = null;

        // cancel timers and change state
        cancelTimers();
        currentState = State.STOPPED;

        // unset life in the controller
        try { lifeViewController.setLife(null); } // let's unset life}
        catch (InvalidPositionException e) { e.printStackTrace(); return false; }

        return true;
    }

    /**
     * @brief cancel all the timers but don't nullify the lifeViewController
     */
    @Override
    public boolean pause() {
        cancelTimers();
        currentState = State.PAUSED;
        return true;
    }

    @Override
    public boolean unpause() {
        boolean x = runTimers();
        currentState = State.STARTED;
        return x;
    }

    /**
     * @brief sets the wait period between calls to life.step().
     * If an invalid value is passed (hz <= 0), the period will be set to default.
     *
     * @param freqPercent must be in [0,1]
     */
    @Override
    public void setFrequency(double freqPercent) {
        if (false == Utils.doubleInRange(freqPercent))
            period = DEFAULT_PERIOD;
        else {
            double freq = MIN_FREQUENCY + freqPercent * (MAX_FREQUENCY - MIN_FREQUENCY);
            period = calculatePeriodFromFrequency(freq);

            // after setting the frequency we need to adjust re-adjust the timers,
            // the easiest way is to pause then start
            if (getState() == State.STARTED) {
                pause();
                unpause();
            }
        }
    }

    long count = 0;
    double cum = 0.0f;

    long drawCount = 0;
    double drawCum = 0.0f;

    @Override
    public double getFrequency() {
        return calculateFrequencyFromPeriod(period);
    }

    /** @brief: run the timers to step through life
     *
     * does not change state
     * @return
     */
    private boolean runTimers() {
        if (this.life == null || getState() == State.STARTED)
            return false;
//
//        drawTimer = new Timeline(new KeyFrame(Duration.millis(period), event -> {
//            try {
//                long startTime = System.nanoTime();
//                lifeViewController.draw();
//                long endTime = System.nanoTime();
//                long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
//                drawCum+=duration;
//                System.out.printf("[draw]\tavg: %.2fms\tduration= %.2f ms\n", drawCum/(1000.0f*(++drawCount)), duration/1000.0f);
//            }
//            catch (InvalidPositionException e) { e.printStackTrace();}
//        }));

//        drawTimer.setCycleCount(Timeline.INDEFINITE);
//        drawTimer.play();

        // Consumer
        Thread t = new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(RootController.this.getState() == gui.State.STARTED) {
                    Action action = queue.poll();
                    if (action != null) {
                        try {
                            Platform.runLater(() -> {
                                try {
                                    lifeViewController.draw(action);
                                } catch (InvalidPositionException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        catch(Exception exc) {
                            exc.printStackTrace();
                            return null;
                        }
                    }
                }
                System.out.println("returning.. ");
                return null;
            }
        });

        System.out.println("runTimers with period: " + period);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    long startTime = System.nanoTime();
                    List<Action> actions = life.step();
                    for (Action action : actions) {
//                        System.out.println("offered action to queue");
                        queue.offer(action);
                    }
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
                    cum+=duration;
//                    System.out.printf("[logic]\tavg: %.2fms\tduration= %.2f ms\n", cum/(1000.0f*(++count)), duration/1000.0f);
                } catch (InvalidPositionException e) {
                    e.printStackTrace();
                } catch (AgentIsDeadException e) {
                    e.printStackTrace();
                }
            }
        }, 0, period);

        t.start();


        return true;
    }

    /** @brief cancel all timers effectively stopping the drawing and logic in life */
    private void cancelTimers() {
        if (getState() != State.STOPPED) {
            timer.cancel();
//            drawTimer.stop();
        }
    }

    @Override
    public State getState() {
        return currentState;
    }

}
