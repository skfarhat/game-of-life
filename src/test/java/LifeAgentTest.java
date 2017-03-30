import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 28/03/2017.
 */
public class LifeAgentTest {

    /** @return a subclass of LifeAgent  - reproduce method is not implemented */
    private LifeAgent lifeAgentSubclass() throws AgentIsDeadException {
        LifeAgent agent = new LifeAgent(){
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        return agent;
    }

    /** @return a subclass of LifeAgent with energy passed - reproduce method is not implemented  */
    private LifeAgent lifeAgentSubclass(int energy) throws AgentIsDeadException {
        LifeAgent agent = new LifeAgent(energy){
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        return agent;
    }


    @Test
    public void testAliveAgent() throws AgentIsDeadException {
        Random rand = new Random();
        int energy = 1 + Math.abs(rand.nextInt());
        LifeAgent agent = lifeAgentSubclass(energy);
        assertEquals(energy, agent.getEnergy().intValue());
    }

    @Test
    public void testCanCreateAliveAgentWithoutInitialEnergy() throws AgentIsDeadException {
        LifeAgent agent = lifeAgentSubclass();
        agent.getEnergy();
    }

    @Test
    public void testAgentDiesWithNonPositiveEnergy() throws AgentIsDeadException {
        LifeAgent agent = lifeAgentSubclass();
        assertTrue(agent.isAlive());
        agent.setEnergy(-1);
        assertFalse(agent.isAlive());
    }

    @Test(expected = AgentIsDeadException.class)
    public void testExceptionThrownWhenCreatingLifeAgentWithNegativeInitialEnergy() throws AgentIsDeadException {
        lifeAgentSubclass(-1);
    }

    @Test(expected = AgentIsDeadException.class)
    public void testExceptionThrownWhenSettingEnergyOnDeadLifeAgent() throws AgentIsDeadException {
        LifeAgent agent = null;
        try {
            // it's not okay to fail here
            agent = lifeAgentSubclass();
            agent.setEnergy(0);
        } catch (AgentIsDeadException e) {
            fail();
        }

        // we expect an exception here
        agent.setEnergy(0);
    }

    @Test(expected = AgentIsDeadException.class)
    public void testExceptionIsThrownWhenKillingAnAlreadyDeadLifeAgent() throws AgentIsDeadException {
        LifeAgent agent = lifeAgentSubclass();
        agent.die();
        agent.die(); // exception thrown here
    }

    @Test
    public void testDecreaseEnergy() throws AgentIsDeadException {
        final int initialEnergy = 100;
        final int decreaseBy = new Random().nextInt(initialEnergy);
        final int expectedFinalEnergy = initialEnergy - decreaseBy;

        LifeAgent agent = lifeAgentSubclass(initialEnergy);
        agent.decreaseEnergyBy(decreaseBy);
        assertEquals(expectedFinalEnergy, agent.getEnergy().intValue());
    }

}