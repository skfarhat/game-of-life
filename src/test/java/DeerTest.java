import core.*;
import core.exceptions.AgentIsDeadException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 30/03/2017.
 */
public class DeerTest {

    @Test
    public void tesDefaultDeer() throws AgentIsDeadException {
        new Deer();
    }

    @Test
    public void testDeerWithInitialEnergy() throws AgentIsDeadException {
        new Deer(new Random().nextInt(Integer.MAX_VALUE));
    }

    @Test
    public void testDeerIsAlive() throws AgentIsDeadException {
        Deer deer = new Deer();
        assertTrue(deer.isAlive());
    }

    @Test
    public void testDeerReproductionCreatesNewDeer() throws AgentIsDeadException {
        Deer deer = new Deer();
        LifeAgent agent = deer.reproduce();
        assertTrue(agent instanceof Deer);
    }

    @Test
    public void testDeerConstructorWithPoint() throws AgentIsDeadException {
        Point2D p = Utils.randomPoint(100, 100);
        Deer deer = new Deer(p);
        assertEquals(p, deer.getPos());
    }

    @Test
    public void testToString() throws AgentIsDeadException {
        Deer deer = new Deer();
        String str = deer.toString();
        // make sure the to String contains the word deer
        assertTrue(str.toLowerCase().indexOf("deer") > -1);
    }

    @Test
    public void testConsume() throws AgentIsDeadException {
        Deer deer = new Deer();
        LifeAgent agent = new LifeAgent() {
            @Override
            public LifeAgent reproduce() throws AgentIsDeadException {
                return null;
            }
        };

        deer.consume(agent);

        // ensure the agent dies when it is consumed
        assertFalse(agent.isAlive());
    }

    @Test
    public void testConsumeAll() throws AgentIsDeadException {
        Deer deer = new Deer();

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
        deer.consumeAll(consumables);

        // check/test
        for (int i = 0; i < N; i++) {
            assertFalse(((LifeAgent)consumables.get(i)).isAlive());
        }
    }

}