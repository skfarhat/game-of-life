import core.*;
import core.actions.Action;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class LifeTest {

    /** @brief used in double comparisons - check IEEE Floating Point standard for details */
    private static final double DELTA = 0.0000001;

    @Test
    public void testLifeDefaults() throws AgentIsDeadException, GridCreationException, InvalidPositionException {
        Life life = new Life();

        assertEquals(life.E_DEFAULT_INITIAL, life.E_GRASS_INITIAL);
        assertEquals(life.E_DEFAULT_INITIAL, life.E_DEER_INITIAL);
        assertEquals(life.E_DEFAULT_INITIAL, life.E_WOLF_INITIAL);
        assertEquals(life.E_DEFAULT_GAIN, life.E_DEER_GAIN);
        assertEquals(life.E_DEFAULT_GAIN, life.E_WOLF_GAIN);
        assertEquals(life.E_DEFAULT_DECREASE, life.E_STEP_DECREASE);
        assertEquals(life.DEFAULT_GRID_N, life.getGridCols());
        assertEquals(life.DEFAULT_GRID_N, life.getGridRows());
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

        params.put(Life.KEY_MAX_ITERATIONS, rand.nextInt(bound));
        params.put(Life.KEY_GRID_COLS, rand.nextInt(bound));
        params.put(Life.KEY_GRID_ROWS, rand.nextInt(bound));
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
        assertEquals(params.get(Life.KEY_GRID_COLS), life.getGridCols());
        assertEquals(params.get(Life.KEY_GRID_ROWS), life.getGridRows());
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

    @Test
    public void testNoMoreDeerWhenNoFoodAndNoReproduction() throws AgentIsDeadException, GridCreationException, InvalidPositionException {
        Map<String, Number> opts = new HashMap<>();
        int nDeer = 1;
        int eDeer = 10;

        opts.put(Life.KEY_I_DEER, nDeer);
        opts.put(Life.KEY_E_DEER_INITIAL, eDeer);
        opts.put(Life.KEY_R_DEER, 0.0);
        opts.put(Life.KEY_I_WOLF, 0);
        opts.put(Life.KEY_I_GRASS, 0);

        Life life = new Life(opts);

        for (int i = 0; i < eDeer; i++) {
            assertEquals(1, life.getAgents().size());
            life.step();
        }
        assertEquals(0, life.getAgents().size());
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
    public void testStepReturnsEmptyListWhenNoAgentsToAct() throws AgentIsDeadException, GridCreationException, InvalidPositionException {
        Map<String, Number> opts = new HashMap<>();

        opts.put(Life.KEY_I_DEER, 0);
        opts.put(Life.KEY_I_WOLF, 0);
        opts.put(Life.KEY_I_GRASS, 0);

        Life life = new Life(opts);
        List<Action> actions = life.step();
        assertEquals(0, actions.size());
    }

    @Test
    public void testMoveToAdjacentCell() {
        // TODO;
    }

    /**
     * test that aims to replicate the concurrent modification exception observed
     * the exception is thrown more regularly when the number of agents increase and the period between step()
     * runs decreases.
     */
    @Test
    public void testReplicateConcurrentModificationException() throws AgentIsDeadException, InvalidPositionException, InterruptedException, GridCreationException {
        final long period = 10;

        Map<String, Number> opts = new HashMap<>();
        opts.put(Life.KEY_I_DEER,   100);
        opts.put(Life.KEY_I_WOLF,   100);
        opts.put(Life.KEY_I_GRASS,  100);
        opts.put(Life.KEY_MAX_ITERATIONS, -1); // let's ensure we won't exceed the  maxIterations counters

        Life life = new Life(opts);

        class BoolVal { boolean running = true; }
        BoolVal bool = new BoolVal();

        // in another thread, read the some cells' agents s
        Thread t = new Thread(() -> {
            while(bool.running) {
                Point2D randomPoint = Utils.randomPoint(life.getGridRows(), life.getGridCols());
                try {
                    LifeCell cell = (LifeCell) life.getGrid().get(randomPoint);
                    Iterator<LifeAgent> it = cell.getAgents();
                    while(it.hasNext()) {
                        it.next();
                        it.remove();
                    }
                } catch (InvalidPositionException e) {
                    e.printStackTrace();
                }
            }

        });
        t.start();

        long testDuration = 10 * 1000; // 10 seconds
        long startTime = System.currentTimeMillis();
        while((System.currentTimeMillis() - startTime) < testDuration) {
            life.step();
            Thread.sleep(period);
        }

        bool.running = false;
    }

    @Test
    public void testRun() throws AgentIsDeadException, GridCreationException, InvalidPositionException {
        Life life = new Life();

        for (int i = 0; i < 30; i++) {
            try {
                life.step();
            } catch (InvalidPositionException e) {
                e.printStackTrace();
            } catch (AgentIsDeadException e) {
                e.printStackTrace();
            }
        }
    }
}