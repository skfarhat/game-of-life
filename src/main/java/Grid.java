import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Sami on 31/03/2017.
 */
public class Grid<T extends Positionable> {
    private Positionable[][] grid;

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
        grid = (T[][]) Array.newInstance(c, rows, cols);

        // set all the correct positions
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                // TODO: check
                // note: this is fiddly because we are trying to create instances of a generic type..
                grid[i][j] = createNewPositionable(c, i, j);
            }
        }
    }

    /**
     *
     * @param p point at which the positionable occurs
     * @return a Positionable
     * @throws InvalidPositionException
     */
    public Positionable get(Point2D p) throws InvalidPositionException {
        return get(p.getX(), p.getY());
    }

    /**
     *
     * @param x coordinate of the Positionable
     * @param y coordinate of the Positionable
     * @return Positionable
     * @throws InvalidPositionException if the given x and y values are out of bounds
     */
    public Positionable get(int x, int y) throws InvalidPositionException {
        if (!xIsInBounds(x) || !yIsInBounds(y))
            throw new InvalidPositionException("The position (" + x + ", " + y + ") is out of bounds.");

        return grid[x][y];
    }

    /** @brief creates a new cell from @param Class<T>.
     * Exceptions are caught and GridCreationException is thrown with the correct message.
     * @param c
     * @param i Integer first param into the c Class
     * @param j Integer second param into the c Class
     * @return a class overriding the Positionable interface
     * @throws GridCreationException to replace one of the following exceptions: IllegalAccessException,
     * IllegalAccessException, InvocationTargetException, NoSuchMethodException (the exception message is passed)
     */
    private T createNewPositionable(Class<T> c, int i, int j) throws GridCreationException {
        try {
            return c.getConstructor(Integer.class, Integer.class).newInstance(i,j);
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

    /** @brief find an adjacent cell to this one, adjacent cells mean one square to the left, right, up or down.
     * No diagonals.
     *  @param p
     * @return
     * @throws InvalidPositionException if the given point is out of bounds
     */
    public Point2D findAdjacentPoint(Point2D p) throws InvalidPositionException {

        get(p); // called to ensure the point is in bounds

        boolean isX = Utils.getRand().nextBoolean();
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

    /** return true if the @param x value passed is in the bounds */
    private boolean xIsInBounds(int x) { return (x > -1 &&  x < rows); }

    /** return true if the @param y value passed is in the bounds */
    private boolean yIsInBounds(int y) { return (y > -1 &&  y < cols); }
}
