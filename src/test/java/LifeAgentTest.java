import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 28/03/2017.
 */
public class LifeAgentTest {

    @Test
    public void testAliveAgent() throws AgentIsDeadException {
        Random rand = new Random();
        int energy = 1 + rand.nextInt(); // ensure it is positive
        LifeAgent agent = new LifeAgent(energy);
        assertEquals(energy, agent.getEnergy().intValue());
    }

    @Test
    public void testCanCreateAliveAgentWithoutInitialEnergy() throws AgentIsDeadException {
        LifeAgent agent = new LifeAgent();
        agent.getEnergy();
    }

    @Test
    public void testAgentDiesWithNonPositiveEnergy() throws AgentIsDeadException {
        LifeAgent agent = new LifeAgent();
        assertTrue(agent.isAlive());
        agent.setEnergy(-1);
        assertFalse(agent.isAlive());
    }
    @Test(expected = AgentIsDeadException.class)
    public void testExceptionThrownWhenSettingEnergyOnDeadLifeAgent() throws AgentIsDeadException {
        LifeAgent agent = null;
        try {
            // it's not okay to fail here
            agent = new LifeAgent();
            agent.setEnergy(0);
        } catch (AgentIsDeadException e) {
            fail();
        }

        // we expect an exception here
        agent.setEnergy(0);
    }

}