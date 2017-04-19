import core.Point2D;
import core.Utils;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 01/04/2017.
 */
public class UtilsTest {
    @Test
    public void getRand() throws Exception {
        assertTrue((Utils.getRand() instanceof Random));
    }

    @Test
    public void randomPositiveInteger() throws Exception {

        // because we're testing a Random method it's difficult to assert with tests that it will always work
        // we therefore check for a number of 'tries'
        final int tries = 100;

        for (int i = 0; i < tries; i++){
            int x = Utils.randomPositiveInteger();
            assertTrue(x >= 0);
        }
    }

    @Test
    public void testRandomPositiveIntegerInRange1() {
        final Integer nb = 10;
        assertEquals(nb, Utils.randomIntegerInRange(nb, nb));
    }

    @Test
    public void testRandomPositiveIntegerInRange2() {
        final Integer min = 10;
        final Integer max = 20;
        // generate 100 numbers and ensure they are all between min and max
        final int n = 100;
        for (int i = 0; i < n; i++) {
            Integer rand = Utils.randomIntegerInRange(min, max);
            assertTrue(rand <= max);
            assertTrue(rand >= min);
        }
    }

    @Test
    public void testRandomPositiveIntegerInRange3() {
        final Integer min = 10;
        final Integer max = 20;
        // generate 100 numbers and ensure they are all between min and max
        final int n = 100;
        // we want to make sure that in 100 tries the min and the max value are generated.
        // There is always a chance that they may not, but with 100 tries they should and we want to know if they don't anyway.
        boolean maxGenerated = false;
        boolean minGenerated = false;
        for (int i = 0; i < n; i++) {
            Integer rand = Utils.randomIntegerInRange(min, max);
            if (rand == min)
                minGenerated = true;
            if (rand == max)
                maxGenerated = true;
        }
        assertTrue(minGenerated);
        assertTrue(maxGenerated);
    }

    @Test
    public void testRandomPointsAreDifferent() {
        int x = 32, y = 32;
        Point2D p1 = Utils.randomPoint(x, y);
        Point2D p2 = Utils.randomPoint(x, y);
        assertNotEquals(p1, p2);
    }
    @Test
    public void randomString() throws Exception {
        String randString = Utils.randomString();
        assertTrue(randString.length() > 0);
        assertNotNull(randString);
    }

    /**
     *  a hack to call the private constructor
     * mostly for code coverage completeness
     */
    @Test
    public void testConstructorIsPrivate() throws Exception {
        Constructor constructor = Utils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIfDoubleOutOfRange() {
        double x = Utils.randomIntegerInRange(1, 1000);
        Utils.exceptionIfOutOfRange(x);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIfDoubleIsNegative() {
        double x = -Utils.randomIntegerInRange(0, 1000); // negative
        Utils.exceptionIfOutOfRange(x);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIfDoubleIsNegative2() {
        double x = -Utils.getRand().nextDouble(); // in range [-1;0]
        Utils.exceptionIfOutOfRange(x);
    }
}