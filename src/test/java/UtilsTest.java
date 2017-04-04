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
    public void randomString() throws Exception {
        String randString = Utils.randomString();
        assertTrue(randString.length() > 0);
        assertNotNull(randString);
    }

    /**
     * @brief a hack to call the private constructor
     * mostly for code coverage completeness
     */
    @Test
    public void testConstructorIsPrivate() throws Exception {
        Constructor constructor = Utils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}