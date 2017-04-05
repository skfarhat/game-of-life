package gui;

import core.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.Iterator;

/**
 * Created by Sami on 04/04/2017.
 */
public class CellView extends Pane {

    Object mutex = new Object();

    // TODO(sami): clean
    public final static int MINICELL_ROWS = 3;
    public final static int MINICELL_COLS = 3;
//    private ImageView[][] miniCells = new ImageView[MINICELL_ROWS][MINICELL_COLS];

    /** @brief the width of the cell sides' width */
    public final static double LINE_WIDTH = 1;

    /** @brief the default side value in px */
    private final static double DEFAULT_SIDE_PX = 128;

    /** @brief the minimum accpetable value for side */
    private final static double MIN_SIDE_PX = 16;

    /** @brief the maximum accpetable value for side */
    private final static double MAX_SIDE_PX = 320;

    /** @brief the value of this CellView's side */
    private final double side;

    private final Line topLine;
    private final Line bottomLine;
    private final Line leftLine;
    private final Line rightLine;
    private final double miniCellSide;

    private Cell cell;

    public CellView(Cell cell) { this(cell, DEFAULT_SIDE_PX); }

    private void addGrass() {
        setStyle("-fx-background-color: green");
    }

    private void removeGrass() {
        setStyle("-fx-background-color: none");
    }

    public CellView(Cell cell, double side) {
        this.cell = cell;
        this.side = side;
        this.miniCellSide = this.side / 3.0f;

        topLine = new Line(0, 0, side, 0);
        bottomLine = new Line(0, side, side, side);
        leftLine = new Line(0, 0, 0, side);
        rightLine = new Line(side, 0, side, side);


        draw();
    }

    public void draw() {

        // TODO(sami): recheck mutex
        synchronized (mutex) {
            getChildren().clear();

            // TODO:
            // not ideal to add and remove those every time
            getChildren().add(topLine);
            getChildren().add(bottomLine);
            getChildren().add(leftLine);
            getChildren().add(rightLine);

            // organise agents
            int agentsCount = cell.agentsCount();
            Iterator<Agent> it = cell.getAgents();

            boolean grassThere = false;
            int addedCount = 0;
            for (int i = 0; i < agentsCount; i++) {

                // if we already encountered the grass agent AND drawn 9 (Wolves or Deers)
                // then there is nothing more to draw, so we break
                if (addedCount > 9 && grassThere) {
                    break;
                }

                Image img = null;
                Agent agent = it.next();

                // TODO(sami): probably doesn't work when ROWSS != COLS -- fix
                int row = (addedCount % MINICELL_ROWS);
                int col = (addedCount / MINICELL_COLS);
                double x = miniCellSide * row;
                double y = miniCellSide * col;

                if(agent instanceof Wolf) {
                    img = new WolfView((Wolf) agent, miniCellSide, miniCellSide);
                }
                else if(agent instanceof Deer) {
                    img = new DeerView((Deer) agent, miniCellSide, miniCellSide);
                }
                else if (agent instanceof Grass && grassThere == false) {
                    addGrass();
                    grassThere = true;
                }

                if (img != null && addedCount < 10) {
                    ImageView imgView = new ImageView(img);
                    imgView.setLayoutX(x);
                    imgView.setLayoutY(y);
                    getChildren().add(imgView);
                    addedCount++;
                }
            }

            // if at the end of the loop no grass was found, then we remove it.
            // we could have removed it before the loop (had it been there or not) and then re-added it
            // in the loop when it was encountered. However, this will likely make cells blink a lot, and
            // so it's better to just remove it when we are sure it is no longer there.
            if (!grassThere)
                removeGrass();
        }

    }

}
