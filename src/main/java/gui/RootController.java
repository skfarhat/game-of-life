package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.net.URL;
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
        System.out.println(lifeViewController);
        System.out.println(controlPaneController);
    }
}
