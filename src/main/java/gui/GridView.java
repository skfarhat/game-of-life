package gui;

import core.*;
import core.actions.*;
import core.exceptions.InvalidPositionException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GridView extends Pane {

    private Map<Point2D, CellView> cells = new HashMap<>();

    private Grid grid;


    // e.g. we are given 800x800px
    // for (row, col) = 100, 80
    // cellSide =  min(8, 10) = 8
    public GridView(Grid grid, double totalWidth, double totalHeight) throws InvalidPositionException {
        this.grid = grid;


        // a single cell's side
        double cellSide = Math.min(totalWidth/grid.getRows(), totalHeight/grid.getCols());

        // calculate the top, bottom, right and left margins
        double marginTopBottom = (totalHeight - cellSide * grid.getRows()) / 2;
        double marginRightLeft = (totalWidth  - cellSide * grid.getCols()) / 2;

        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        for (int i = 0; i < grid.getCols(); i++) {
            for (int j = 0; j < grid.getRows(); j++) {

                Point2D p = new Point2D(i, j);

                CellView cellView = new CellView(grid.get(p), cellSide);

                double x = marginRightLeft + i * cellSide;
                double y = marginTopBottom + j * cellSide;

                cellView.setLayoutX(x);
                cellView.setLayoutY(y);

//                cellView.setStyle("-fx-background-color: " + toRGBCode(colors.get(i)));
                // shade colors
                // if ((i % 2 == 0 && j % 2 == 1) || (i % 2 == 1 && j % 2 == 0))
                //   cellView.setStyle("-fx-background-color:yellow");

                getChildren().add(cellView);
                cells.put(p, cellView);
            }
        }
    }

    public void drawParallel() {
        int threads = 4;
        ObservableList<Node> children = getChildren();
        int n = children.size();

        int i1 = n / 4;
        int i2 = 2 * (n / 4);
        int i3 = 3 * (n / 4);
//        assert(i1 == (i2 - i1) && i1 == (i3 - i2) && i1 == (n - i3));
        compute(children, 0, i1);
        compute(children, i1, i2);
        compute(children, i2, i3);
        compute(children, i3, n);
    }

    private void compute(ObservableList<Node> children, int start, int end) {
        Platform.runLater(() -> {
            for (int i = start ; i < end; i++) {
                Node node = children.get(i);
                if (node instanceof CellView){
                    ((CellView)node).draw();
                }
            }
        });
    }

    // TODO(sami): can we parallelise this
    public void draw() {
        getChildren().stream().forEach(cellView -> {
            if(cellView instanceof CellView)
                ((CellView)cellView).draw();
        });
    }

    public void draw(Action action)  {
        if (action instanceof Age) {
            Age age = (Age) action;
            Point2D srcPt = age.getAgent().getPos();
            CellView srcCell = cells.get(srcPt);
            srcCell.draw();
        }
        else if (action instanceof Move) {
            Move move = (Move) action;
            Point2D srcPt = move.getFrom();
            Point2D dstPt = move.getTo();

            CellView srcCell = cells.get(srcPt);
            CellView dstCell = cells.get(dstPt);

            srcCell.draw();
            dstCell.draw();
        }
        else if (action instanceof Consume) {
            Point2D pt = ((Consume) action).getAgent().getPos();
            CellView cellView = cells.get(pt);
            cellView.draw();
        }
        else if (action instanceof Reproduce) {
            Reproduce reproduce = (Reproduce) action;
            Point2D pt = reproduce.getAgent().getPos();

            // draw the agent's cell
            CellView agentCell = cells.get(pt);
            agentCell.draw();

            // draw all cells where babies are located (except the agent's cell)
            Iterator<LifeAgent> it = reproduce.getBabies();
            while(it.hasNext()) {
                Point2D p = it.next().getPos();
                cells.get(p).draw();
            }
        }

    }
}
