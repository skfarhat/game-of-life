package core;

import core.exceptions.GridCreationException;
import core.exceptions.InvalidPositionException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;


public class Grid<T extends Cell> {

    private Cell[][] grid;

    private int rows;
    private int cols;

    /**
     * * <pre>
     * {@code
     * Grid grid = new Grid<Cell>(Cell.class, 5, 6);
     * }
     * </pre>
     *
     * @param c class type of T
     * @param rows number of rows in the grid
     * @param cols number of columns in the grid
     */
    public Grid(Class<T> c, int rows, int cols) throws GridCreationException {
        this.rows = rows;
        this.cols = cols;

        // creating an 2d array of generics is fiddly
        grid = new Cell[rows][cols];

        // set all the correct positions
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = createNewCellSubclass(c, new Point2D(j, i));
            }
        }
    }

    /**
     * @param p point at which the positionable occurs
     * @return a Positionable
     * @throws InvalidPositionException
     */
    public Cell get(Point2D p) throws InvalidPositionException {
        return get(p.getX(), p.getY());
    }

    /**
     *
     * @param x coordinate of the Positionable
     * @param y coordinate of the Positionable
     * @return Positionable
     * @throws InvalidPositionException if the given x and y values are out of bounds
     */
    public Cell get(int x, int y) throws InvalidPositionException {
        if (!xIsInBounds(x) || !yIsInBounds(y))
            throw new InvalidPositionException("The position (" + x + ", " + y + ") is out of bounds.");

        return grid[y][x];
    }

    /**
     *
     *  creates a new cell from @param Class<T>.
     * Exceptions are caught and GridCreationException is thrown with the correct message.
     *
     * @param c class type to return an  instance of
     * @param p
     *
     * @return a class overriding the Positionable interface
     * @throws GridCreationException to replace one of the following exceptions: IllegalAccessException,
     * IllegalAccessException, InvocationTargetException, NoSuchMethodException (the exception message is passed)
     */
    private T createNewCellSubclass(Class<T> c, Point2D p) throws GridCreationException {
        try {
            return c.getConstructor(Point2D.class).newInstance(p);
        } catch (InstantiationException e) {
            throw new GridCreationException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new GridCreationException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new GridCreationException(e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new GridCreationException(e.getMessage());
        }
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

    /**
     *  moves an agent from its source cell to the @param dstCell
     * @param agent
     * @param dstCell
     */
    public boolean moveAgentToCell(Agent agent, Cell dstCell) throws InvalidPositionException {
        Cell srcCell = get(agent.getPos());

        // remove from src cell
        Iterator<Agent> it = srcCell.getAgents();

        while(it.hasNext())
            if (agent == it.next())
                it.remove();

        // add to dst cell and changes the position of the agent to that of the cell
        return dstCell.addAgent(agent);
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    /** @return true if the point @param p passed is in the bounds of the grid */
    public boolean pointInBounds(Point2D p) {
        return xIsInBounds(p.getX()) && yIsInBounds(p.getY());
    }

    /** return true if the @param x value passed is in the bounds */
    public boolean xIsInBounds(int x) { return (x > -1 &&  x < cols); }

    /** return true if the @param y value passed is in the bounds */
    public boolean yIsInBounds(int y) { return (y > -1 &&  y < rows); }
}
