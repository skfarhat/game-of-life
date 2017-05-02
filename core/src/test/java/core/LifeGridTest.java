package core;

import core.exceptions.AgentAlreadyDeadException;
import core.exceptions.GridCreationException;
import core.exceptions.InvalidPositionException;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class LifeGridTest {

    @Test(expected = GridCreationException.class)
    public void testGridWithZeroCols() {
        new LifeGrid(Utils.randomIntegerInRange(5,50),0);
    }


    @Test(expected = GridCreationException.class)
    public void testGridWithZeroRows() {
        new LifeGrid(0, Utils.randomIntegerInRange(5,50));
    }

    @Test(expected = GridCreationException.class)
    public void testGridWithZeroRowsAndCols() {
        new LifeGrid(0,0);
    }

    @Test
    public void testGridWithOneCell() {
        new LifeGrid(1,1);
    }

    @Test
    public void testFindAdjacentPointWhenGridIsOneCell() {
        LifeGrid g = new LifeGrid(1,1);
        Point2D p = g.get(0,0).getPos(); // the only cell
        Point2D adjacent = g.randomAdjacentPoint(p); // adjacent to the only cell

        // the adjacent to the only point should be itself
        assertEquals(p, adjacent);
    }

    @Test
    public void testGetCols() {
        final int cols = Utils.randomIntegerInRange(5, 50);
        final int rows = Utils.randomIntegerInRange(5, 50);
        LifeGrid grid = new LifeGrid(rows, cols);
        assertEquals(cols, grid.getCols());
    }

    @Test
    public void testGetRows() {
        final int rows = Utils.randomIntegerInRange(5, 50);
        final int cols = Utils.randomIntegerInRange(5, 50);
        LifeGrid grid = new LifeGrid(rows, cols);
        assertEquals(rows, grid.getRows());
    }

    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenXTooHigh() throws GridCreationException, InvalidPositionException {
        final int rows = 5;
        final int cols = 10;
        LifeGrid grid = new LifeGrid(rows, cols);

        grid.get(0, rows);
    }

    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenYTooHigh() throws GridCreationException, InvalidPositionException {
        final int rows = 10;
        final int cols = 5;
        LifeGrid grid = new LifeGrid(rows, cols);

        grid.get(cols, 0);
    }
    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenXTooLow() throws GridCreationException, InvalidPositionException {
        final int rows = 5;
        final int cols = 5;
        LifeGrid grid = new LifeGrid(rows, cols);

        grid.get(-1, 0);
    }
    @Test(expected = InvalidPositionException.class)
    public void invalidExceptionThrownWhenYTooLow() throws GridCreationException, InvalidPositionException {
        final int rows = 5;
        final int cols = 5;
        LifeGrid grid = new LifeGrid(rows, cols);

        grid.get(0, -1);
    }

    @Test
    public void testPointInBounds() {
        final int rows = Utils.randomIntegerInRange(5, 50);
        final int cols = Utils.randomIntegerInRange(5, 50);
        LifeGrid grid = new LifeGrid(rows, cols);

        assertFalse(grid.isPointInBounds(new Point2D(cols, 0))); // x out of bounds
        assertFalse(grid.isPointInBounds(new Point2D(0, rows)));  // y out of bounds
        assertFalse(grid.isPointInBounds(new Point2D(cols, rows)));  // both out of bounds
        assertFalse(grid.isPointInBounds(new Point2D(-1, -1)));  // both out of bounds
    }

    @Test
    public void randomAdjacentPoint() throws GridCreationException, InvalidPositionException {

        final int rows = Utils.randomIntegerInRange(5, 50);
        final int cols = Utils.randomIntegerInRange(5, 50);
        LifeGrid grid = new LifeGrid(rows, cols);
        final int tries = 30;
        for (int i = 0; i < tries; i++) {

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
    public void testFindAdjacentCell() {
        final int tries = 30;
        final int rows = Utils.randomIntegerInRange(5, 50);
        final int cols = Utils.randomIntegerInRange(5, 50);
        LifeGrid grid = new LifeGrid(rows, cols);
        for (int i = 0; i < tries; i++) {

            Point2D p = Utils.randomPoint(cols, rows);
            LifeCell adjacentCell = grid.randomAdjacentCell(grid.get(p));
            Point2D adjacent = adjacentCell.getPos();

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
    public void testFindAdjacentCellWithTwoRows() {
        final int tries = 30;
        final int rows = 2;
        final int cols = Utils.randomIntegerInRange(5, 50);
        LifeGrid grid = new LifeGrid(rows, cols);
        for (int i = 0; i < tries; i++) {
            Point2D p = Utils.randomPoint(cols, rows);
            LifeCell adjacentCell = grid.randomAdjacentCell(grid.get(p));
            Point2D adjacent = adjacentCell.getPos();

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
        LifeGrid grid = new LifeGrid(rows, cols);
        Point2D p = Utils.randomPoint(cols, rows);
        Point2D adjacent = grid.randomAdjacentPoint(p);
        assertEquals(p, adjacent);
    }

    @Test
    public void randomAdjacentPointRowsIs1() throws GridCreationException, InvalidPositionException {
        final int rows = 1;
        final int cols = Utils.randomIntegerInRange(2, 10);
        LifeGrid grid = new LifeGrid(rows, cols);

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
        LifeGrid grid = new LifeGrid(rows, cols);

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
        LifeGrid grid = new LifeGrid(rows, cols);
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

        // we should find the agent in the dstCell's core.agents list
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