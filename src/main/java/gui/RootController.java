package gui;

import core.AgentIsDeadException;
import core.GridCreationException;
import core.InvalidPositionException;
import core.Life;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @class RootContoller
 */
public class RootController implements Initializable {

    @FXML private ScrollPane controlPane;
    @FXML private ControlPanelController controlPaneController;

    @FXML private Pane lifeView;
    @FXML private LifeViewController lifeViewController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        Map<String, Number> options = controlPaneController.getOptions();
        Life life = null;

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

        try {
            lifeViewController.draw();
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
        ;
    }
}
