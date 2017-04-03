import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Sami on 29/03/2017.
 */
public class LifeTest {

    /** @brief used in double comparisons - check IEEE Floating Point standard for details */
    private static final double DELTA = 0.0000001;

    @Test
    public void testLifeDefaults() throws GridCreationException, AgentIsDeadException, InvalidPositionException {
        Life life = new Life();

        assertEquals(life.E_DEFAULT_INITIAL, life.E_GRASS_INITIAL);
        assertEquals(life.E_DEFAULT_INITIAL, life.E_DEER_INITIAL);
        assertEquals(life.E_DEFAULT_INITIAL, life.E_WOLF_INITIAL);
        assertEquals(life.E_DEFAULT_GAIN, life.E_DEER_GAIN);
        assertEquals(life.E_DEFAULT_GAIN, life.E_WOLF_GAIN);
        assertEquals(life.E_DEFAULT_DECREASE, life.E_STEP_DECREASE);
        assertEquals(life.DEFAULT_GRID_N, life.getGridSize());
        assertEquals(life.R_DEFAULT, life.R_WOLF, DELTA);
        assertEquals(life.I_DEFAULT, life.I_GRASS);
        assertEquals(life.I_DEFAULT, life.I_DEER);
        assertEquals(life.I_DEFAULT, life.I_WOLF);
        assertEquals(life.R_DEFAULT, life.R_GRASS, DELTA);
        assertEquals(life.R_DEFAULT, life.R_DEER, DELTA);
    }

    @Test
    public void testLifeWithOverriddenValues() throws GridCreationException, InvalidPositionException, AgentIsDeadException {
        Map<String, Number> params = new HashMap<String, Number>();
        Random rand = Utils.getRand();

        // just an int bound used for some of the rands
        final int bound = 60;

        params.put(Life.KEY_GRID_N, rand.nextInt(bound));
        params.put(Life.KEY_E_GRASS_INITIAL, Utils.randomPositiveInteger(bound));
        params.put(Life.KEY_E_DEER_INITIAL, Utils.randomPositiveInteger(bound));
        params.put(Life.KEY_E_WOLF_INITIAL, Utils.randomPositiveInteger(bound));
        params.put(Life.KEY_E_DEER_GAIN, Utils.randomPositiveInteger(bound));
        params.put(Life.KEY_E_WOLF_GAIN, Utils.randomPositiveInteger(bound));
        params.put(Life.KEY_E_STEP_DECREASE, Utils.randomPositiveInteger(bound));
        params.put(Life.KEY_I_GRASS, rand.nextInt(bound));
        params.put(Life.KEY_I_DEER, rand.nextInt(bound));
        params.put(Life.KEY_I_WOLF, rand.nextInt(bound));
        params.put(Life.KEY_R_GRASS, rand.nextDouble());
        params.put(Life.KEY_R_DEER, rand.nextDouble());
        params.put(Life.KEY_R_WOLF, rand.nextDouble());

        Life life = new Life(params);
        assertEquals(params.get(Life.KEY_GRID_N), life.getGridSize());
        assertEquals(params.get(Life.KEY_E_GRASS_INITIAL), life.E_GRASS_INITIAL);
        assertEquals(params.get(Life.KEY_E_DEER_INITIAL), life.E_DEER_INITIAL);
        assertEquals(params.get(Life.KEY_E_WOLF_INITIAL), life.E_WOLF_INITIAL);
        assertEquals(params.get(Life.KEY_E_DEER_GAIN), life.E_DEER_GAIN);
        assertEquals(params.get(Life.KEY_E_WOLF_GAIN), life.E_WOLF_GAIN);
        assertEquals(params.get(Life.KEY_E_STEP_DECREASE), life.E_STEP_DECREASE);
        assertEquals(params.get(Life.KEY_I_GRASS), life.I_GRASS);
        assertEquals(params.get(Life.KEY_I_DEER), life.I_DEER);
        assertEquals(params.get(Life.KEY_I_WOLF), life.I_WOLF);
        assertEquals(params.get(Life.KEY_R_GRASS), life.R_GRASS);
        assertEquals(params.get(Life.KEY_R_DEER), life.R_DEER);
        assertEquals(params.get(Life.KEY_R_WOLF), life.R_WOLF);
    }

    private double randDoubleOutOfRange() {
        Random rand = Utils.getRand();
        final int RANGE = rand.nextInt();
        double val;
        do {
            val = (-RANGE) + 2*RANGE*rand.nextDouble();
        } while(val < 1 && val > 0);
        return val;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLifeWithInvalidRGrassValueThrowsException() throws GridCreationException, InvalidPositionException, AgentIsDeadException {
        // choose a random double not between 0-1
        double val = randDoubleOutOfRange();

        Map<String, Number> params = new HashMap<String, Number>();
        params.put(Life.KEY_R_DEER, val);
        new Life(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLifeWithInvalidRDEERValueThrowsException() throws GridCreationException, InvalidPositionException, AgentIsDeadException {
        // choose a random double not between 0-1
        double val = randDoubleOutOfRange();

        Map<String, Number> params = new HashMap<String, Number>();
        params.put(Life.KEY_R_DEER, val);
        new Life(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLifeWithInvalidRWolfValueThrowsException() throws GridCreationException, InvalidPositionException, AgentIsDeadException {
        // choose a random double not between 0-1
        double val = randDoubleOutOfRange();

        Map<String, Number> params = new HashMap<String, Number>();
        params.put(Life.KEY_R_WOLF, val);
        new Life(params);
    }

    @Test
    public void testMoveToAdjacentCell() {
        // TODO;
    }

    @Test
    public void testRecycleDeadAgentsFromCell() throws AgentIsDeadException, GridCreationException, InvalidPositionException {
        Life life = new Life();

        // create a list of agents (N = [10,100])
        ArrayList<LifeAgent> originalAgents = new ArrayList<>();
        final int N = 10 + Utils.randomPositiveInteger(91);
        for (int i = 0; i < N; i++) {
            originalAgents.add(new LifeAgent() {
                @Override
                public LifeAgent reproduce() throws AgentIsDeadException {
                    return null;
                }
            });
         }

        // copy originalAgents into agentsStillAlive
        // will have agents who die removed from it
        final ArrayList<LifeAgent> agentsStillAlive = new ArrayList<>(); // will hold all the remaining alive agents
        agentsStillAlive.addAll(originalAgents);

        // kill a random number of those agents
        final int killCount = 1 + Utils.randomPositiveInteger(N-1);

        ArrayList<LifeAgent> agentsKilled = new ArrayList<>(); // will hold all the agents that were killed
        for (int i = 0; i < killCount; i++) {

            // find an agent that hasn't been killed yet
            int index = Utils.randomPositiveInteger(agentsStillAlive.size());

            // kill them
            LifeAgent agentToKill = agentsStillAlive.get(index);
            agentToKill.die();

            // add and remove them from the concerned lists
            assertTrue(agentsKilled.add(agentToKill));
            assertTrue(agentsStillAlive.remove(agentToKill));
        }

        assertEquals("agentsKilled array doesn't have the correct end size", killCount, agentsKilled.size());
        assertEquals("agentsStillAlive array doesn't have the correct end size", N-killCount, agentsStillAlive.size());

        // call the method we are testing
        int actuallyKilled = life.recycleDeadAgentsFromCell(originalAgents);
        assertEquals("the number of killed agents is not correct", killCount, actuallyKilled);
        assertEquals("the final size of the list is not correct", N-killCount, originalAgents.size());

        // make sure all the agents in 'agentsKilled' are dead
        agentsKilled.stream().forEach(a -> {
            assertFalse(a.isAlive()); // agent should be dead
            assertFalse(originalAgents.contains(a));
        });

    }

}