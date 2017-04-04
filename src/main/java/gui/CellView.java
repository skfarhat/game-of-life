package gui;

import core.Cell;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

/**
 * Created by Sami on 04/04/2017.
 */
public class CellView extends Pane {

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

    private Cell cell;

    public CellView(Cell cell) { this(cell, DEFAULT_SIDE_PX); }

    public CellView(Cell cell, double side) {
        this.cell = cell;
        this.side = side;

        topLine = new Line(0, 0, side, 0);
        bottomLine = new Line(0, side, side, side);
        leftLine = new Line(0, 0, 0, side);
        rightLine = new Line(side, 0, side, side);

        getChildren().add(topLine);
        getChildren().add(bottomLine);
        getChildren().add(leftLine);
        getChildren().add(rightLine);
    }

}
