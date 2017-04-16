package gui;

import core.*;
import core.actions.*;
import core.exceptions.InvalidPositionException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.*;

public class GridView extends Pane {

    private Map<Point2D, CellView> cells = new HashMap<>();

    private Grid grid;

    /**
     *  e.g. we are given 800x800px
     *  for (row, col) = 100, 80
     *  cellSide =  min(8, 10) = 8
     *
     * @param grid
     * @param totalWidth
     * @param totalHeight
     * @throws InvalidPositionException
     */
    public GridView(Grid grid, double totalWidth, double totalHeight) throws InvalidPositionException {
        this.grid = grid;


        // a single cell's side
        double cellSide = Math.min(totalWidth/grid.getRows(), totalHeight/grid.getCols());

        // calculate the top, bottom, right and left margins
        double marginTopBottom = (totalHeight - cellSide * grid.getRows()) / 2;
        double marginRightLeft = (totalWidth  - cellSide * grid.getCols()) / 2;

        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        for (int j = 0; j < grid.getRows(); j++) {
            for (int i = 0; i < grid.getCols(); i++) {


                Point2D p = new Point2D(i, j);

                CellView cellView = new CellView(grid.get(p), cellSide);

                double x = marginRightLeft + i * cellSide;
                double y = marginTopBottom + j * cellSide;

                cellView.setLayoutX(x);
                cellView.setLayoutY(y);

                getChildren().add(cellView);
                cells.put(p, cellView);
            }
        }
    }

    /** @brief draw all cells */
    public void drawAll() {
        getChildren().stream().forEach(cellView -> {
            if(cellView instanceof CellView)
                ((CellView)cellView).draw();
        });
    }

    /** @brief redraw all cells affected by any one of the actions in the @param actions list */
    public void draw(List<Action> actions)  {
        for (Action action : actions)
            draw(action);
    }

    /** @brief redraw the cells affected by @param action. This depends on what the action is exactly. */
    public void draw(Action action)  {
        if (action instanceof EnergyChange) {
            EnergyChange age = (EnergyChange) action;
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

    /** @brief counts the number of agents added to GridView - useful for debugging */
    private int countAgents() throws InvalidPositionException {
        int total = 0;
        for (Node node: getChildren()) {
            if (node instanceof CellView) {
                CellView cell = (CellView) node;
                total +=(cell.getChildren().size() - 4);
            }
        }
        return total;
    }
}
