import core.*;
import core.exceptions.AgentAlreadyDeadException;
import core.exceptions.GridCreationException;
import core.exceptions.InvalidPositionException;
import org.junit.Test;

import java.util.Iterator;
import java.util.Random;

import static org.junit.Assert.*;

public class GridTest {

    @Test
    public void testGridConstructor() throws GridCreationException, InvalidPositionException {
        Random rand = Utils.getRand();
        final int bound = 50;
        int rows = Utils.randomIntegerInRange(5, 50);
        int cols = Utils.randomIntegerInRange(5, 50);
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Point2D p = grid.get(j, i).getPos();
                assertEquals(j, p.getX());
                assertEquals(i, p.getY());
            }
        }
    }

    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenXTooHigh() throws GridCreationException, InvalidPositionException {
        final int rows = 5;
        final int cols = 10;
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

        grid.get(0, rows);
    }

    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenYTooHigh() throws GridCreationException, InvalidPositionException {
        final int rows = 10;
        final int cols = 5;
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

        grid.get(cols, 0);
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

            final int rows = Utils.randomIntegerInRange(5, 50);
            final int cols = Utils.randomIntegerInRange(5, 50);
            Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

            Point2D p = Utils.randomPoint(cols, rows);
            Point2D adjacent = grid.randomAdjacentPoint(p);

            // (xDiff == 1) XOR (yDiff == 1)
            boolean valid = 1 == Math.abs((adjacent.getX() - p.getX())) ^ 1 == Math.abs((adjacent.getY() - p.getY()));
            assertTrue(valid);

            // the difference might be one and test can be done by here
            // but let's just add an extra insurance, the below should not
            // throw an exception
            grid.get(adjacent);
        }
    }

    @Test
    public void randomAdjacentPoint1CellReturnsItself() throws GridCreationException, InvalidPositionException {
        final int rows = 1;
        final int cols = 1;
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

        Point2D p = Utils.randomPoint(cols, rows);
        Point2D adjacent = grid.randomAdjacentPoint(p);
        assertEquals(p, adjacent);
    }

    @Test
    public void randomAdjacentPointRowsIs1() throws GridCreationException, InvalidPositionException {
        final int rows = 1;
        final int cols = Utils.randomIntegerInRange(2, 10);
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

        Point2D p = Utils.randomPoint(cols, rows);
        Point2D adjacent = grid.randomAdjacentPoint(p);

        // the difference in the x valus between source and adjacent is 1
        int xDiff = Math.abs((adjacent.getX() - p.getX()));
        assertEquals(1, xDiff);
    }

    @Test
    public void randomAdjacentPointColsIs1() throws GridCreationException, InvalidPositionException {
        final int rows = Utils.randomIntegerInRange(2, 10);
        final int cols = 1;
        Grid<Cell> grid = new Grid<Cell>(Cell.class, rows, cols);

        Point2D p = Utils.randomPoint(cols, rows);
        Point2D adjacent = grid.randomAdjacentPoint(p);

        // the difference in the x valus between source and adjacent is 1
        int yDiff = Math.abs((adjacent.getY() - p.getY()));
        assertEquals(1, yDiff);
    }


    @Test
    public void testMoveAgentToCell() throws GridCreationException, InvalidPositionException, AgentAlreadyDeadException {
        final int rows = Utils.randomIntegerInRange(5, 15);
        final int cols = Utils.randomIntegerInRange(5, 15);
        Grid<Cell> grid = new Grid<>(Cell.class, rows, cols);
        Cell srcCell = grid.get(Utils.randomPoint(cols, rows));
        Cell dstCell = grid.get(grid.randomAdjacentPoint(srcCell.getPos()));
        Agent a = new LifeAgent(100) {
            @Override
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
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




