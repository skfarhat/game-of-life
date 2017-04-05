package gui;

import core.AgentIsDeadException;
import core.GridCreationException;
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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @class RootContoller
 */
public class RootController implements Initializable {

    @FXML private ScrollPane controlPane;
    @FXML private ControlPanelController controlPaneController;

    @FXML private Pane lifeView;
    @FXML private LifeViewController lifeViewController;

    private boolean running = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Life life = null;

        running = true;
        try {
            life = new Life();
        } catch (GridCreationException e) {
            e.printStackTrace();
        } catch (AgentIsDeadException e) {
            e.printStackTrace();
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
        lifeViewController.setLife(life);
        lifeViewController.setRootPane(lifeView);
        System.out.println(Thread.currentThread().getName());

        long period = 100;
        Life finalLife1 = life;
        new Timer().schedule(new TimerTask() {
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
    }
}
