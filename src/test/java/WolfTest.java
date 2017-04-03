import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Sami on 28/03/2017.
 */
public class WolfTest {

    @Test
    public void testWolfIsAlive() throws AgentIsDeadException {
        Wolf wolf = new Wolf();
        assertTrue(wolf.isAlive());
    }

    @Test
    public void testWolfHasCorrectInitialEnergy() throws AgentIsDeadException {
        final int initialEnergy = 100;
        Wolf wolf = new Wolf(initialEnergy);
        assertEquals(initialEnergy, wolf.getEnergy().intValue());
    }

    @Test
    public void testWolfReproductionCreatesNewWolf() throws AgentIsDeadException {
        Wolf wolf = new Wolf();
        LifeAgent agent = wolf.reproduce();
        assertTrue(agent instanceof Wolf);
    }

    @Test
    public void testWolfAgesCorrectly() throws AgentIsDeadException {
        final int initialEnergy = 100;
        final int ageBy = 10;
        Wolf wolf = new Wolf(initialEnergy);
        wolf.ageBy(ageBy);
        assertEquals(initialEnergy-ageBy, wolf.getEnergy().intValue());
    }

    @Test
    public void testToString() throws AgentIsDeadException {
        Wolf wolf = new Wolf();
        String str = wolf.toString();
        // make sure the to String contains the wolf wolf
        assertTrue(str.toLowerCase().indexOf("wolf") > -1);
    }

    @Test
    public void testConsume() throws AgentIsDeadException {
        Wolf wolf = new Wolf();
        LifeAgent agent = new LifeAgent() {
            @Override
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };

        wolf.consume(agent);

        // ensure the agent dies when it is consumed
        assertFalse(agent.isAlive());
    }

    @Test
    public void testConsumeAll() throws AgentIsDeadException {
        Wolf wolf = new Wolf();

        // setup
        final int N = 20;
        ArrayList<Consumable> consumables = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            LifeAgent agent = new LifeAgent() {
                @Override
                public LifeAgent reproduce() throws AgentIsDeadException {
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