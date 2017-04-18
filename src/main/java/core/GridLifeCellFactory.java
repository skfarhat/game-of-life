/**
 * Created by Sami on 01/04/2017.
 */
package core;

import core.exceptions.GridCreationException;

/**
 *  Factory class to construct a Grid of Cells
 */
public class GridLifeCellFactory {

    /**  private constructor */
    private GridLifeCellFactory() {}

    /**
     * @param rows
     * @param cols
     * @return null on failure
     */
    public static Grid<LifeCell> createGridCell(int rows, int cols) throws GridCreationException {
        Grid<LifeCell> grid = new Grid<LifeCell>(LifeCell.class, rows, cols);
        return grid;
    }
}
