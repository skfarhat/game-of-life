package core;

import core.exceptions.GridCreationException;
import core.exceptions.InvalidPositionException;

import java.util.Iterator;

/**
 *
 */
public class LifeGrid {
    /**
     *
     */
    private LifeCell[][] grid;

    /**
     * number of rows in the grid
     */
    private int rows;

    /**
     * number of columns in the grid
     */
    private int cols;

    /**
     * constructor
     * @param rows
     * @param cols
     */
    public LifeGrid(int rows, int cols) {
        if (rows == 0 || cols == 0)
            throw new GridCreationException();

        this.rows = rows;
        this.cols = cols;

        // create and initialise cells in the grid
        grid = new LifeCell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new LifeCell(new Point2D(j, i));
            }
        }
    }

    /**
     * @param p point at which the positionable occurs
     * @return a Positionable
     * @throws InvalidPositionException
     */
    public LifeCell get(Point2D p) {
        return get(p.getX(), p.getY());
    }

    /**
     *
     * @param x coordinate of the Positionable
     * @param y coordinate of the Positionable
     * @return Positionable
     * @throws InvalidPositionException if the given x and y values are out of bounds
     */
    public LifeCell get(int x, int y) throws InvalidPositionException {
        if (!xIsInBounds(x) || !yIsInBounds(y))
            throw new InvalidPositionException("The position (" + x + ", " + y + ") is out of bounds.");

        return grid[y][x];
    }

    /**
     * moves an agent from its source cell to the @param dstCell
     * @param c
     * @param dstCell
     */
    public boolean moveCeature(Creature c, Cell dstCell) {
        Cell srcCell = get(c.getPos());

        // remove from src cell
        Iterator<Agent> it = srcCell.getAgents();

        while(it.hasNext())
            if (c == it.next())
                it.remove();

        // add to dst cell and changes the position of the agent to that of the cell
        return dstCell.addAgent(c);
    }

    /**
     * find random adjacent cell to the given cell
     * @param c
     * @return
     * @throws InvalidPositionException
     */
    public LifeCell randomAdjacentCell(Cell c) throws InvalidPositionException {
        return get(randomAdjacentPoint(c.getPos()));
    }

    /**
     * find an adjacent cell to this one, adjacent cells mean one square to the left, right, up or down.
     * No diagonals.
     *  @param p
     * @return
     * @throws InvalidPositionException if the given point is out of bounds
     */
    public Point2D randomAdjacentPoint(Point2D p) throws InvalidPositionException {

        get(p); // called to ensure the point is in bounds

        // 1 cell
        if (1 == cols && 1 == rows)
            return new Point2D(p);

        boolean isX;

        // 1 row only
        if (1 == rows)
            isX = true;
            // 1 column only
        else if (1 == cols)
            isX = false;
            // many rows and columns, pick randomly along which access we should move
        else
            isX = Utils.getRand().nextBoolean();

        boolean positive = Utils.getRand().nextBoolean();
        int nextX = p.getX(), nextY = p.getY();

        // x
        if (isX) {
            nextX =  nextX  + ((positive)? 1 : -1);
            if (!xIsInBounds(nextX)) nextX = p.getX()  + ((positive)? -1 : 1); // if we are at an edge, go the other way
        }
        // y
        else {
            nextY = nextY + ((positive)? 1 : -1);
            if (!yIsInBounds(nextY)) nextY = p.getY()  + ((positive)? -1 : 1); // if we are at an edge, go the other way

        }
        return new Point2D(nextX, nextY);
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    /** @return true if the point @param p passed is in the bounds of the grid */
    public boolean isPointInBounds(Point2D p) {
        return xIsInBounds(p.getX()) && yIsInBounds(p.getY());
    }

    /** return true if the @param x value passed is in the bounds */
    public boolean xIsInBounds(int x) { return (x > -1 &&  x < cols); }

    /** return true if the @param y value passed is in the bounds */
    public boolean yIsInBounds(int y) { return (y > -1 &&  y < rows); }

}
