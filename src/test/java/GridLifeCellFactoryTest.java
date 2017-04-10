import core.GridLifeCellFactory;
import core.exceptions.GridCreationException;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 01/04/2017.
 */
public class GridLifeCellFactoryTest {

    /**
     * @brief a hack to call the private constructor
     * mostly for code coverage completeness
     */
    @Test
    public void testConstructorIsPrivate() throws Exception {
        Constructor constructor = GridLifeCellFactory.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void testCreateGridCell() throws GridCreationException {
        Random rand = new Random(System.currentTimeMillis());
        final int bound = 100;
        final int rows = rand.nextInt(bound);
        final int cols = rand.nextInt(bound);

        // if no exception is thrown we consider the test to have succeeded
        GridLifeCellFactory.createGridCell(rows, cols);
    }

}