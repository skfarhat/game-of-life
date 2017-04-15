import core.Point2D;
import core.Utils;
import core.exceptions.AgentIsDeadException;
import core.LifeAgent;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 28/03/2017.
 */
public class LifeAgentTest {

    /** @return a subclass of core.LifeAgent  - reproduce method is not implemented */
    private LifeAgent lifeAgentSubclass() throws AgentIsDeadException {
        LifeAgent agent = new LifeAgent(){
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        return agent;
    }

    /** @return a subclass of core.LifeAgent with position (Point2D) passed - reproduce method is not implemented  */
    private LifeAgent lifeAgentSubclass(Point2D p) throws AgentIsDeadException {
        LifeAgent agent = new LifeAgent(p){
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        return agent;
    }

    /** @return a subclass of core.LifeAgent with energy passed - reproduce method is not implemented  */
    private LifeAgent lifeAgentSubclass(int energy) throws AgentIsDeadException {
        LifeAgent agent = new LifeAgent(energy){
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        return agent;
    }
    /** @return a subclass of core.LifeAgent with position and energy passed - reproduce method is not implemented  */
    private LifeAgent lifeAgentSubclass(Point2D p, int energy) throws AgentIsDeadException {
        LifeAgent agent = new LifeAgent(p, energy){
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };
        return agent;
    }

    @Test
    public void testCanUseReflectionOnStaticGetDefaultParams() throws NoSuchMethodException {
        LifeAgent.class.getMethod(LifeAgent.METHOD_NAME_GET_DEFAULT_PARAMS).invoke();
    }

    @Test
    public void testAliveAgent() throws AgentIsDeadException {
        Random rand = new Random();
        int energy = 1 + Math.abs(rand.nextInt());
        LifeAgent agent = lifeAgentSubclass(energy);
        assertEquals(energy, agent.getEnergy().intValue());
    }

    @Test
    public void testConstructor1Param() throws AgentIsDeadException {
        Point2D p =  Utils.randomPoint(100,100);
        LifeAgent agent = lifeAgentSubclass(p);
        assertEquals(p, agent.getPos());
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
    public void testExceptionThrownWhenCreatingLifeAgentWithNegativeInitialEnergy2() throws AgentIsDeadException {
        lifeAgentSubclass(new Point2D(0, 0), -Utils.randomPositiveInteger(100));
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
    public void testEnegyChange() throws AgentIsDeadException {
        final int initialEnergy = 100;
        final int delta = Utils.randomPositiveInteger(initialEnergy*2 ) - initialEnergy;
        final int expectedFinalEnergy = initialEnergy + delta;

        LifeAgent agent = lifeAgentSubclass(initialEnergy);
        agent.changeEnergyBy(delta);
        assertEquals(expectedFinalEnergy, agent.getEnergy().intValue());
    }

}