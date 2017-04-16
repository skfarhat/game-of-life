import core.GridLifeCellFactory;
import core.Utils;
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
        final int rows = Utils.randomIntegerInRange(5, 100);
        final int cols = Utils.randomIntegerInRange(5, 100);

        // if no exception is thrown we consider the test to have succeeded
        GridLifeCellFactory.createGridCell(rows, cols);
    }

}