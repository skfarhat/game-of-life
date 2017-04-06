package gui;

import core.AgentIsDeadException;
import core.InvalidPositionException;
import core.Life;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @class RootController
 *
 * handles only one life at a time
 */
public class RootController implements Initializable, LifeStarter {

    /** @brief period between calls to step() in msec */
    private final static int DEFAULT_PERIOD = 100;

    // ===========================================================================================
    // FXML
    // ===========================================================================================

    @FXML private ScrollPane controlPane;
    @FXML private ControlPanelController controlPaneController;

    @FXML private Pane lifeView;
    @FXML private LifeViewController lifeViewController;

    private Life life;

    // ===========================================================================================
    // MEMBER VARIABLES
    // ===========================================================================================

//    private boolean running = false;
    private int period = DEFAULT_PERIOD; // milliseconds
    private Timer timer;
    private Timeline drawTimer;
    private State currentState = State.STOPPED;

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
     * @param freq frequency value bete
     */
    @Override
    public void setFrequency(double freq) {
        // set to default if invalid value passed
        if (freq <= 0.0f)
            period = DEFAULT_PERIOD;
        else
            period = (int) (1000 * (1.0/freq));

        // after setting the frequency we need to adjust re-adjust the timers,
        // the easiest way is to pause then start
        pause();
        start(this.life);
    }

    /** @brief: run the timers to step through life
     *
     * does not change state
     * @return
     */
    private boolean runTimers() {
        if (this.life == null || getState() == State.STARTED)
            return false;

        drawTimer = new Timeline(new KeyFrame(Duration.millis(period), event -> {
            try { lifeViewController.draw(); }
            catch (InvalidPositionException e) { e.printStackTrace();}
        }));

        drawTimer.setCycleCount(Timeline.INDEFINITE);
        drawTimer.play();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    life.step();
                } catch (InvalidPositionException e) {
                    e.printStackTrace();
                } catch (AgentIsDeadException e) {
                    e.printStackTrace();
                }
            }
        }, 0, period);
        return true;
    }

    /** @brief cancel all timers effectively stopping the drawing and logic in life */
    private void cancelTimers() {
        if (getState() != State.STOPPED) {
            timer.cancel();
            drawTimer.stop();
        }
    }

    @Override
    public State getState() {
        return currentState;
    }

}
