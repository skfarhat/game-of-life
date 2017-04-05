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

    public LifeViewController() {}

    public void setLife(Life life) { this.life = life; }

    public void setRootPane(Pane rootPane) {
        this.rootPane = rootPane;
    }

    public void draw() throws InvalidPositionException {
        GridView gridView = new GridView(life.getGrid(), rootPane.getPrefWidth(), rootPane.getPrefHeight());
        gridView.setStyle("-fx-background-color=cyan;");
        rootPane.getChildren().add(gridView);
    }
}
