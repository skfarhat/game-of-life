package gui;

import core.*;
import core.actions.Action;
import core.exceptions.AgentIsDeadException;
import core.exceptions.InvalidPositionException;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

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
    private final static int MAX_PERIOD = 200;      // 200 milliseconds

    // calculate the frequency ranges fom the (min, max) period
    private static final double MIN_FREQUENCY = calculateFrequencyFromPeriod(MAX_PERIOD);
    private static final double MAX_FREQUENCY = calculateFrequencyFromPeriod(MIN_PERIOD);

    // ===========================================================================================
    // MEMBER VARIABLES
    // ===========================================================================================

    private int period = DEFAULT_PERIOD; // milliseconds
    private Timer timer;
    private State currentState = State.STOPPED;
    private Life life;
    private ConcurrentLinkedQueue<Action> queue = new ConcurrentLinkedQueue<>();
    private SimpleIntegerProperty iterations = new SimpleIntegerProperty();

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
        if (false == core.Utils.doubleInRange(freqPercent))
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

    @Override
    public SimpleIntegerProperty getIterationsObservable() {
        return iterations;
    }

    /** @brief: run the timers to step through life
     *
     * does not change state
     * @return
     */
    private boolean runTimers() {
        if (this.life == null || getState() == State.STARTED)
            return false;

        // Consumer
        Thread t = new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(RootController.this.getState() == gui.State.STARTED) {
                    Action action = queue.poll();

                    // if we find an action, then there are likely more actions coming
                    // we wait for 1msec (TODO(sami): should be configurable) and then try to get more actions
                    // to send in batch to the draw function
                    if (action != null) {
                        Thread.sleep(1);
                        List<Action> actions = new ArrayList<>();
                        actions.add(action);
                        while(queue.peek()!=null) {
                            actions.add(queue.poll());
                        }
                        try {
                            Platform.runLater(() -> {
                                try { lifeViewController.draw(actions);}
                                catch (InvalidPositionException e) {e.printStackTrace();}
                            });
                        }
                        catch(Exception exc) {
                            exc.printStackTrace();
                            return null;
                        }
                    }
                }
                return null;
            }
        });

        System.out.println("runTimers with period: " + period);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    List<Action> actions = life.step();
//                    System.out.println("agents size: "  + life.getAgents().size());
                    iterations.setValue(life.getIteration());

                    for (Action action : actions) {
                        queue.offer(action);
                    }

                    // cancel timer if max iterations exceeded
                    if (life.getMaxIterations() > 0 &&  life.getIteration() >= life.getMaxIterations()) {
                        cancelTimers();
                    }

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

    /**
     * @return the current state of the system (e.g. Started, Stopped, Paused)
     */
    @Override
    public State getState() {
        return currentState;
    }

    /**
     * @return lifeGetter interface to expose some of the getter methods in life without handing the full refrenece to it
     */
    public LifeGetter lifeGetter() {
        return life;
    }

    /** @brief cancel all timers effectively stopping the drawing and logic in life */
    private void cancelTimers()  {
        if (getState() != State.STOPPED) {
            timer.cancel();
        }
        try {
            System.out.println("counted drawn agents: " + lifeViewController.countAgents());
            lifeViewController.draw();
            System.out.println("counted drawn agents: " + lifeViewController.countAgents());
//            System.out.println("counted agents: " + life.getAgents().size());
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
//        Platform.runLater(() -> lifeViewController.draw());
    }

}
