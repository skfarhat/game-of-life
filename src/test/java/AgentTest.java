import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sami on 28/03/2017.
 */
public class AgentTest {

    @Test
    public void testAgentHasId() {
        Agent agent = new Agent();
        assertNotNull(agent.getId());
    }
}