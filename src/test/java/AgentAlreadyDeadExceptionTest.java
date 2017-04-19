import core.exceptions.AgentAlreadyDeadException;
import core.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sami on 30/03/2017.
 */
public class AgentAlreadyDeadExceptionTest {
    @Test
    public void testDefaultException() {
        try {
            throw new AgentAlreadyDeadException();
        }
        catch (AgentAlreadyDeadException exc) {
            assertTrue(true); // pass
        }
    }

    @Test
    public void testExceptionWithMessage() {
        final String message = Utils.randomString();
        try {
            throw new AgentAlreadyDeadException(message);
        }
        catch (AgentAlreadyDeadException exc) {
            assertEquals(message, exc.getMessage());
        }
    }
}