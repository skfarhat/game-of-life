import core.exceptions.AgentIsDeadException;
import core.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sami on 30/03/2017.
 */
public class AgentIsDeadExceptionTest {
    @Test
    public void testDefaultException() {
        try {
            throw new AgentIsDeadException();
        }
        catch (AgentIsDeadException exc) {
            assertTrue(true); // pass
        }
    }

    @Test
    public void testExceptionWithMessage() {
        final String message = Utils.randomString();
        try {
            throw new AgentIsDeadException(message);
        }
        catch (AgentIsDeadException exc) {
            assertEquals(message, exc.getMessage());
        }
    }
}