import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 31/03/2017.
 */
public class GridTest {

    @Test
    public void testGridConstructor() throws InvalidPositionException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Random rand = new Random(System.currentTimeMillis());
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

}