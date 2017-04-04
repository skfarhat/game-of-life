/**
 * Created by Sami on 01/04/2017.
 */
package core;

/**
 * @class Factory class to construct a Grid of Cells
 */
public class GridCellFactory {

    /** @brief private constructor */
    private GridCellFactory() {}

    /**
     * @param rows
     * @param cols
     * @return null on failure
     */
    public static Grid<Cell> createGridCell(int rows, int cols) throws GridCreationException {
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);
        return grid;
    }
}
