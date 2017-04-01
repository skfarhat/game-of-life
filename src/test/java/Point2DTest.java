import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 30/03/2017.
 */
public class Point2DTest {

    @Test
    public void testCreateNewPoint() {
        Random rand = new Random(System.currentTimeMillis());
        final int x = rand.nextInt();
        final int y = rand.nextInt();
        Point2D p = new Point2D(x, y);

        assertEquals(x, p.getX());
        assertEquals(y, p.getY());
    }

    @Test
    public void testPoint2DEqualsWorksWhenXYAreSame() {
        Random rand = new Random(System.currentTimeMillis());
        final int x = rand.nextInt();
        final int y = rand.nextInt();

        // they are the same
        Point2D p1 = new Point2D(x, y);
        Point2D p2 = new Point2D(x, y);

        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
    }

    @Test
    public void testPoint2DEqualsWorksWhenXYenAreDifferent() {
        Random rand = new Random(System.currentTimeMillis());
        final int x = rand.nextInt();
        final int y = rand.nextInt();
        int diff = 1 + rand.nextInt(100);

        // they are different
        Point2D p1 = new Point2D(x, y);
        Point2D p2 = new Point2D(x, y + diff);

        assertFalse(p1.equals(p2));
        assertFalse(p2.equals(p1));
    }

    @Test
    public void testEqualsWithNullParam() {
        Random rand = new Random(System.currentTimeMillis());
        final int x = rand.nextInt();
        final int y = rand.nextInt();
        int diff = 1 + rand.nextInt(100);

        // they are different
        Point2D p1 = new Point2D(x, y);
        assertFalse(p1.equals(null));
    }

    @Test
    public void testEqualsWithItself() {
        Random rand = new Random(System.currentTimeMillis());
        final int x = rand.nextInt();
        final int y = rand.nextInt();
        int diff = 1 + rand.nextInt(100);

        // they are different
        Point2D p1 = new Point2D(x, y);
        assertTrue(p1.equals(p1));
    }

    @Test
    public void testSetWorks() {
        Random rand = new Random(System.currentTimeMillis());
        final int x = rand.nextInt();
        final int y = rand.nextInt();

        final int x2 = rand.nextInt();
        final int y2 = rand.nextInt();

        Point2D p1 = new Point2D(x, y);
        Point2D p2 = new Point2D(x2, y2);
        p1.set(p2);

        assertEquals(x2, p1.getX());
        assertEquals(y2, p1.getY());
    }

    @Test
    public void testSetXWorks() {
        Random rand = new Random(System.currentTimeMillis());
        final int x = rand.nextInt();
        final int y = rand.nextInt();
        final int x2 = rand.nextInt();

        Point2D p1 = new Point2D(x, y);
        assertEquals(x, p1.getX());
        p1.setX(x2);
        assertEquals(x2, p1.getX());
    }

    @Test
    public void testSetYWorks() {
        Random rand = new Random(System.currentTimeMillis());
        final int x = rand.nextInt();
        final int y = rand.nextInt();
        final int y2 = rand.nextInt();

        Point2D p1 = new Point2D(x, y);
        assertEquals(y, p1.getY());
        p1.setY(y2);
        assertEquals(y2, p1.getY());
    }

}