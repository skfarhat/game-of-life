package gui;

import core.Grid;
import core.InvalidPositionException;
import javafx.geometry.NodeOrientation;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 * Created by Sami on 04/04/2017.
 */
public class GridView extends Pane {

    public GridView(Grid grid, double totalWidth, double totalHeight) throws InvalidPositionException {

        // e.g. we are given 800x800px
        // for (row, col) = 100, 80
        // cellSide =  min(8, 10) = 8

        // a single cell's side
        double cellSide = Math.min(totalWidth/grid.getRows(), totalHeight/grid.getCols());

        // calculate the top, bottom, right and left margins
        double marginTopBottom = (totalHeight - cellSide * grid.getRows()) / 2;
        double marginRightLeft = (totalWidth  - cellSide * grid.getCols()) / 2;

        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        for (int i = 0; i < grid.getCols(); i++) {
            for (int j = 0; j < grid.getRows(); j++) {

                CellView cellView = new CellView(grid.get(i, j), cellSide);

                double x = marginRightLeft + i * cellSide;
                double y = marginTopBottom + j * cellSide;
                System.out.println(i + ", " + j + " --> x, y" + x + ", " + y);

                cellView.setLayoutX(x);
                cellView.setLayoutY(y);

                // shade colors
                // if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0))
                //   cellView.setStyle("-fx-background-color:yellow");

                getChildren().add(cellView);
            }
        }

    }
}
