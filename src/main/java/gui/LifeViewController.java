package gui;

import core.exceptions.InvalidPositionException;
import core.Life;
import core.actions.Action;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * @class LifeViewController
 */
public class LifeViewController {

    @FXML private Pane rootPane;

    private Life life;

    private GridView gridView;

    public LifeViewController(){}

    public void setLife(Life life) throws InvalidPositionException {

        // only update the Pane if the passed life is non-null and different from the previous s
        if (life != null && life != this.life) {
            this.gridView = new GridView(life.getGrid(), rootPane.getPrefWidth(), rootPane.getPrefHeight());

            // clear the pane then add the gridview
            rootPane.getChildren().clear();
            rootPane.getChildren().add(gridView);
        }

        this.life = life;
    }

    public void setRootPane(Pane rootPane) {
        this.rootPane = rootPane;
    }

    public void draw() {
        gridView.draw();
    }

    public void draw(Action action) throws InvalidPositionException {
        gridView.draw(action);
    }
}
