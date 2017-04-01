import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 31/03/2017.
 */
public class GridTest {

    @Test
    public void testGridConstructor() throws GridCreationException, InvalidPositionException {

        Random rand = Utils.getRand();
        final int bound = 50;
        int rows = 5 + rand.nextInt(bound);
        int cols = 5 + rand.nextInt(bound);
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Point2D p = grid.get(i, j).getPos();
                assertEquals(i, p.getX());
                assertEquals(j, p.getY());
            }
        }
    }

    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenXTooHigh() throws GridCreationException, InvalidPositionException {
        final int rows = 5;
        final int cols = 5;
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

        grid.get(rows, 0);
    }

    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenYTooHigh() throws GridCreationException, InvalidPositionException {
        final int rows = 5;
        final int cols = 5;
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

        grid.get(0, cols);
    }
    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenXTooLow() throws GridCreationException, InvalidPositionException {
        final int rows = 5;
        final int cols = 5;
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

        grid.get(-1, 0);
    }
    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenYTooLow() throws GridCreationException, InvalidPositionException {
        final int rows = 5;
        final int cols = 5;
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

        grid.get(0, -1);
    }

    @Test
    public void findAdjacentPoint() throws GridCreationException, InvalidPositionException {
        final int rows = 5 + Utils.randomPositiveInteger(46); // rows between (5,50)
        final int cols = 5 + Utils.randomPositiveInteger(46); // rows between (5,50)
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);
        System.out.println(rows + ", " + cols);
        Point2D p = Utils.randomPoint(rows, cols);
        
        System.out.println(rows + ", " + cols);
        Point2D adjacent = grid.findAdjacentPoint(p);

        System.out.println("p is " + p);
        System.out.println("adjacent is " + adjacent);

        // (xdiff == 1) XOR (yDiff == 1)
        boolean valid = 1 == Math.abs((adjacent.getX() - p.getX())) ^  1 == Math.abs((adjacent.getY() - p.getY()));
        assertTrue(valid);

        // the difference might be one and test can be done by here
        // but let's just add an extra insurance, the below should not
        // throw an exception
        grid.get(adjacent);
    }

}




