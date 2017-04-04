import core.GridCreationException;
import core.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sami on 01/04/2017.
 */
public class GridCreationExceptionTest {
    @Test
    public void testDefaultException() {
        try {
            throw new GridCreationException();
        }
        catch (GridCreationException exc) {
            assertTrue(true); // pass
        }
    }

    @Test
    public void testExceptionWithMessage() {
        final String message = Utils.randomString();
        try {
            throw new GridCreationException(message);
        }
        catch (GridCreationException exc) {
            assertEquals(message, exc.getMessage());
        }
    }
}