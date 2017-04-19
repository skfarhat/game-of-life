import core.Point2D;
import core.Utils;
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
    public void testConstructorByCopy() {
        Point2D p1 = Utils.randomPoint(30, 30);
        Point2D p2 = new Point2D(p1);
        assertTrue(p1.getX() == p2.getX());
        assertTrue(p1.getY() == p2.getY());
        assertEquals(p1, p2); // this is redundant but might as well
    }

    /**
     * ensure equals works when we are comparing a subclass with its parent as long as they have the same x and y
     */
    @Test
    public void testEqualsWorksWhenSubclassOfPoint() {
        class Point3D extends Point2D {

            public Point3D(int x, int y) {
                super(x, y);
            }
            public Point3D(Point2D p) {
                super(p);
            }
        };
        Point2D p1 = Utils.randomPoint(30, 30);
        Point3D p2 = new Point3D(p1);
        assertEquals(p1, p2);
    }

    @Test
    public void testEqualsDoesntWorkWithDifferentClasses() {
        // should fail because Integer is not a subclass of Point2DD
        assertFalse(Utils.randomPoint(30, 30).equals(new Integer(30)));
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
        Point2D p1 = new Point2D(x, y);
        assertFalse(p1.equals(null));
    }

    @Test
    public void testEqualsWithItself() {
        Random rand = new Random(System.currentTimeMillis());
        final int x = rand.nextInt();
        final int y = rand.nextInt();
        Point2D p1 = new Point2D(x, y);
        assertTrue(p1.equals(p1));
    }


    @Test
    public void testToString() {
        // let's make sure the string at least contains the x and y values.
        Point2D p = Utils.randomPoint(100, 100);
        final String str = p.toString();
        assertNotNull(str);
        assert(str.indexOf(Integer.toString(p.getX())) > -1); // make sure 'x' is in the String
        assert(str.indexOf(Integer.toString(p.getY())) > -1); // make sure 'y' is in the String
    }

    @Test
    public void sameHashcodeForEqualPoints() {
        final int x = Utils.randomPositiveInteger(100);
        final int y = Utils.randomPositiveInteger(100);
        Point2D p1 = new Point2D(x, y);
        Point2D p2 = new Point2D(x, y);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}