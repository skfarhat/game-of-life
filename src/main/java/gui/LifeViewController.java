package gui;

import core.InvalidPositionException;
import core.Life;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Created by Sami on 04/04/2017.
 */
public class LifeViewController {

    @FXML private Pane rootPane;

    Life life;

    public LifeViewController() {
        System.out.println("LifeViewController:constructor");
    }

    public void setLife(Life life) { this.life = life; }

    public void draw() throws InvalidPositionException {
        // draw the grid
        CellView cellView = new CellView(life.getGrid().get(0,0));
        rootPane.getChildren().add(cellView);
        rootPane.setStyle("-fx-background-color:yellow");
    }
}
