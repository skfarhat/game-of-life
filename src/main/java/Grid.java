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
    public Grid(Class<T> c, int rows, int cols) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        this.rows = rows;
        this.cols = cols;
        System.out.println(rows + " " + cols);
        // creating an 2d array of generics is fiddly
        grid = (T[][]) Array.newInstance(c, rows, cols);

        // set all the correct positions
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                // TODO: check
                // note: this is fiddly because we are trying to create instances of a generic type..
                grid[i][j] = c.getConstructor(Integer.class, Integer.class).newInstance(i,j);
            }
        }
    }

    public Positionable get(int x, int y) throws InvalidPositionException {
        if (x < 0 || x >= rows || y < 0 || y >= cols)
            throw new InvalidPositionException("The position (" + x + ", " + y + ") is out of bounds.");

        return grid[x][y];
    }
}
