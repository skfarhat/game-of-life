package gui;

import core.InvalidPositionException;
import core.Life;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * @class LifeViewController
 */
public class LifeViewController {

    @FXML private Pane rootPane;

    Life life;

    GridView gridView;

    public LifeViewController(){}

    public void setLife(Life life) throws InvalidPositionException {
        this.life = life;
        this.gridView = new GridView(life.getGrid(), rootPane.getPrefWidth(), rootPane.getPrefHeight());
        rootPane.getChildren().add(gridView);
    }

    public void setRootPane(Pane rootPane) {
        this.rootPane = rootPane;
    }

    public void draw() throws InvalidPositionException {
        gridView.draw();
    }
}
