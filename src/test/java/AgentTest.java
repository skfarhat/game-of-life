import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Sami on 28/03/2017.
 */
public class AgentTest {

    @Test
    public void testAgentHasId() {
        Agent agent = new Agent() {
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        assertNotNull(agent.getId());
    }

    @Test
    public void testDefaultAgentIsAt00() {
        Agent agent = new Agent() {
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        assertEquals(Agent.DEFAULT_X_POS, agent.getPos().getX());
        assertEquals(Agent.DEFAULT_Y_POS, agent.getPos().getY());
        assertNotNull(agent.getId());
    }
}