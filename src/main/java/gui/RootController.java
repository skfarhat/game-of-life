package gui;

import core.AgentIsDeadException;
import core.InvalidPositionException;
import core.Life;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
 */
public class RootController implements Initializable, LifeStarter {

    @FXML private ScrollPane controlPane;
    @FXML private ControlPanelController controlPaneController;

    @FXML private Pane lifeView;
    @FXML private LifeViewController lifeViewController;

    private boolean running = false;

    private Timer timer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controlPaneController.setLifeStarter(this);
    }

    public boolean start(Life life) {
        final long period = 100;
        running = true; // TODO(sami): remove this
        lifeViewController.setRootPane(lifeView);
        try {
            lifeViewController.setLife(life);
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }

        Life finalLife1 = life;

        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(period), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    lifeViewController.draw();
                } catch (InvalidPositionException e) {
                    e.printStackTrace();
                }
            }
        }));

        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    finalLife1.step();
                } catch (InvalidPositionException e) {
                    e.printStackTrace();
                } catch (AgentIsDeadException e) {
                    e.printStackTrace();
                }
            }
        }, 0, period);
        return true;
    }

    @Override
    public boolean stop() {
        // TODO(sami): create an excpetion for this
        if (timer != null)
            timer.cancel();
        return false;
    }

    @Override
    public boolean pause() {
//        timer.wait();
        return false;
    }
}
