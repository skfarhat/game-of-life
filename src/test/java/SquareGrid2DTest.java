import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sami on 31/03/2017.
 */
public class SquareGrid2DTest {

    @Test
    public void testDefaultConstructor() {
        SquareGrid2D<Cell> grid = new SquareGrid2D<Cell>(Cell.class, 3);
    }

    @Test
    public void testAllPositionablesInGridHaveCorrectFields() {
        final int N = 4;
        SquareGrid2D<Cell> grid = new SquareGrid2D<Cell>(Cell.class, N);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Point2D p = grid.get(i, j).getPos();
                assertEquals(i, p.getX());
                assertEquals(j, p.getY());
            }
        }
    }

}