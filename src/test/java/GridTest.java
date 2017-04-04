import core.*;
import org.junit.Test;

import java.util.Iterator;
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
    public void randomAdjacentPoint() throws GridCreationException, InvalidPositionException {

        final int tries = 20;
        for (int i = 0; i < tries; i++) {

            final int rows = 5 + Utils.randomPositiveInteger(46); // rows between (5,50)
            final int cols = 5 + Utils.randomPositiveInteger(46); // rows between (5,50)
            Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

            Point2D p = Utils.randomPoint(rows, cols);
            Point2D adjacent = grid.randomAdjacentPoint(p);

            // (xdiff == 1) XOR (yDiff == 1)
            boolean valid = 1 == Math.abs((adjacent.getX() - p.getX())) ^ 1 == Math.abs((adjacent.getY() - p.getY()));
            assertTrue(valid);

            // the difference might be one and test can be done by here
            // but let's just add an extra insurance, the below should not
            // throw an exception
            grid.get(adjacent);
        }
    }

    @Test
    public void testMoveAgentToCell() throws GridCreationException, InvalidPositionException, AgentIsDeadException {
        final int rows = 5 + Utils.randomPositiveInteger(10);
        final int cols = 5 + Utils.randomPositiveInteger(10);
        Grid<Cell> grid = new Grid<>(Cell.class, rows, cols);
        Cell srcCell = grid.get(Utils.randomPoint(rows, cols));
        Cell dstCell = grid.get(grid.randomAdjacentPoint(srcCell.getPos()));
        Agent a = new LifeAgent(100) {
            @Override
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };

        srcCell.addAgent(a);
        assertEquals(1, srcCell.agentsCount());

        assertTrue(grid.moveAgentToCell(a, dstCell));

        // we should find the agent in the dstCell's agents list
        {
            boolean foundInDst = false;
            for (Iterator<Agent> it = dstCell.getAgents(); it.hasNext() && !foundInDst; ) {
                foundInDst = it.next() == a;
            }
            assertTrue(foundInDst);
        }

        assertEquals(0, srcCell.agentsCount());
        assertEquals(1, dstCell.agentsCount());

        // we should not find the agent in the dst
        {
            boolean foundInSrc = false;
            for (Iterator<Agent> it = srcCell.getAgents(); it.hasNext() && !foundInSrc;) {
                foundInSrc = it.next() == a;
            }
            assertFalse(foundInSrc);
        }
    }
}




