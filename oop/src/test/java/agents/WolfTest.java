package agents;

import core.LifeAgent;
import core.Point2D;
import core.Utils;
import core.exceptions.AgentAlreadyDeadException;
import core.interfaces.Consumable;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WolfTest {

    @Test
    public void testWolfIsAlive() throws AgentAlreadyDeadException {
        Wolf wolf = new Wolf();
        assertTrue(wolf.isAlive());
    }

    @Test
    public void testWolfHasCorrectInitialEnergy() throws AgentAlreadyDeadException {
        final int initialEnergy = 100;
        Wolf wolf = new Wolf(initialEnergy);
        assertEquals(initialEnergy, wolf.getEnergy().intValue());
    }

    @Test
    public void testWolfReproductionCreatesNewWolf() throws AgentAlreadyDeadException {
        Wolf wolf = new Wolf();
        LifeAgent agent = wolf.reproduce();
        assertTrue(agent instanceof Wolf);
    }

    @Test
    public void testDeerConstructorWithPoint() throws AgentAlreadyDeadException {
        Point2D p = Utils.randomPoint(100, 100);
        Wolf wolf = new Wolf(p);
        assertEquals(p, wolf.getPos());
    }

    @Test
    public void testToString() throws AgentAlreadyDeadException {
        Wolf wolf = new Wolf();
        String str = wolf.toString();
        // make sure the to String contains the wolf wolf
        assertTrue(str.toLowerCase().indexOf("wolf") > -1);
    }

    @Test
    public void testConsume() throws AgentAlreadyDeadException {
        Wolf wolf = new Wolf();
        LifeAgent agent = new LifeAgent() {
            @Override
            public LifeAgent reproduce() throws AgentAlreadyDeadException {
                return null;
            }
        };

        wolf.consume(agent);

        // ensure the agent dies when it is consumed
        assertFalse(agent.isAlive());
    }

    @Test
    public void testConsumeAll() throws AgentAlreadyDeadException {
        Wolf wolf = new Wolf();

        // setup
        final int N = 20;
        ArrayList<Consumable> consumables = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            LifeAgent agent = new LifeAgent() {
                @Override
                public LifeAgent reproduce() throws AgentAlreadyDeadException {
                    return null;
                }
            };
            consumables.add(agent);
        }

        // consume all
        wolf.consumeAll(consumables);

        // check/test
        for (int i = 0; i < N; i++) {
            assertFalse(((LifeAgent)consumables.get(i)).isAlive());
        }
    }
}