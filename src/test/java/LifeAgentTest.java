import core.Point2D;
import core.Utils;
import core.exceptions.AgentAlreadyDeadException;
import core.LifeAgent;
import core.exceptions.LifeImplementationException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 28/03/2017.
 */
public class LifeAgentTest {

    /** @return a subclass of core.LifeAgent  - reproduce method is not implemented */
    private LifeAgent lifeAgentSubclass() throws AgentAlreadyDeadException {
        LifeAgent agent = new LifeAgent(){
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
                return null;
            }
        };
        return agent;
    }

    /** @return a subclass of core.LifeAgent with position (Point2D) passed - reproduce method is not implemented  */
    private LifeAgent lifeAgentSubclass(Point2D p) throws AgentAlreadyDeadException {
        LifeAgent agent = new LifeAgent(p){
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
                return null;
            }
        };
        return agent;
    }

    /** @return a subclass of core.LifeAgent with energy passed - reproduce method is not implemented  */
    private LifeAgent lifeAgentSubclass(int energy) throws AgentAlreadyDeadException {
        LifeAgent agent = new LifeAgent(energy){
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
                return null;
            }
        };
        return agent;
    }
    /** @return a subclass of core.LifeAgent with position and energy passed - reproduce method is not implemented  */
    private LifeAgent lifeAgentSubclass(Point2D p, int energy) throws AgentAlreadyDeadException {
        LifeAgent agent = new LifeAgent(p, energy){
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
                return null;
            }
        };
        return agent;
    }

    @Test
    public void testCanUseReflectionOnStaticGetDefaultParams() throws NoSuchMethodException, LifeImplementationException {
        try {
            LifeAgent.class.getMethod(LifeAgent.METHOD_NAME_GET_DEFAULT_PARAMS).invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new LifeImplementationException();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new LifeImplementationException();
        }
    }

    @Test
    public void testAliveAgent() throws AgentAlreadyDeadException {
        Random rand = new Random();
        int energy = 1 + Math.abs(rand.nextInt());
        LifeAgent agent = lifeAgentSubclass(energy);
        assertEquals(energy, agent.getEnergy().intValue());
    }

    @Test
    public void testConstructor1Param() throws AgentAlreadyDeadException {
        Point2D p =  Utils.randomPoint(100,100);
        LifeAgent agent = lifeAgentSubclass(p);
        assertEquals(p, agent.getPos());
    }

    @Test
    public void testCanCreateAliveAgentWithoutInitialEnergy() throws AgentAlreadyDeadException {
        LifeAgent agent = lifeAgentSubclass();
        agent.getEnergy();
    }

    @Test
    public void testAgentDiesWithNonPositiveEnergy() throws AgentAlreadyDeadException {
        LifeAgent agent = lifeAgentSubclass();
        assertTrue(agent.isAlive());
        agent.setEnergy(-1);
        assertFalse(agent.isAlive());
    }

    @Test(expected = AgentAlreadyDeadException.class)
    public void testExceptionThrownWhenCreatingLifeAgentWithNegativeInitialEnergy() throws AgentAlreadyDeadException {
        lifeAgentSubclass(-1);
    }

    @Test(expected = AgentAlreadyDeadException.class)
    public void testExceptionThrownWhenCreatingLifeAgentWithNegativeInitialEnergy2() throws AgentAlreadyDeadException {
        lifeAgentSubclass(new Point2D(0, 0), -Utils.randomPositiveInteger(100));
    }

    @Test(expected = AgentAlreadyDeadException.class)
    public void testExceptionThrownWhenSettingEnergyOnDeadLifeAgent() throws AgentAlreadyDeadException {
        LifeAgent agent = null;
        try {
            // it's not okay to fail here
            agent = lifeAgentSubclass();
            agent.setEnergy(0);
        } catch (AgentAlreadyDeadException e) {
            fail();
        }

        // we expect an exception here
        agent.setEnergy(0);
    }



    @Test(expected = AgentAlreadyDeadException.class)
    public void testExceptionIsThrownWhenKillingAnAlreadyDeadLifeAgent() throws AgentAlreadyDeadException {
        LifeAgent agent = lifeAgentSubclass();
        agent.die();
        agent.die(); // exception thrown here
    }

    @Test
    public void testEnegyChange() throws AgentAlreadyDeadException {
        final int initialEnergy = 100;
        final int delta = Utils.randomPositiveInteger(initialEnergy*2 ) - initialEnergy;
        final int expectedFinalEnergy = initialEnergy + delta;

        LifeAgent agent = lifeAgentSubclass(initialEnergy);
        agent.changeEnergyBy(delta);
        assertEquals(expectedFinalEnergy, agent.getEnergy().intValue());
    }

}