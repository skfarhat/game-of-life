import core.*;
import core.actions.Action;
import core.exceptions.AgentIsDeadException;
import core.exceptions.InvalidPositionException;
import core.exceptions.LifeException;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class LifeTest {

    /** @brief used in double comparisons - check IEEE Floating Point standard for details */
    private static final double DELTA = 0.0000001;

    @Test
    public void testLifeDefaults() throws LifeException {
        Life life = new Life();

        assertEquals(life.DEFAULT_E_DEER, life.E_GRASS_INITIAL);
        assertEquals(life.DEFAULT_E_DEER, life.E_DEER_INITIAL);
        assertEquals(life.DEFAULT_E_DEER, life.E_WOLF_INITIAL);
        assertEquals(life.DEFAULT_E_GAIN, life.E_DEER_GAIN);
        assertEquals(life.DEFAULT_E_GAIN, life.E_WOLF_GAIN);
        assertEquals(life.DEFAULT_AGE, life.AGE_DEER);
        assertEquals(life.DEFAULT_AGE, life.AGE_WOLF);
        assertEquals(life.DEFAULT_GRID_N, life.getGridCols());
        assertEquals(life.DEFAULT_GRID_N, life.getGridRows());
        assertEquals(life.DEFAULT_R_WOLF, life.R_WOLF, DELTA);
        assertEquals(life.DEFAULT_R_DEER, life.R_DEER, DELTA);
        assertEquals(life.DEFAULT_I_GRASS, life.I_GRASS);
        assertEquals(life.DEFAULT_I_DEER, life.I_DEER);
        assertEquals(life.DEFAULT_I_WOLF, life.I_WOLF);
    }

    @Test
    public void testLifeWithOverriddenValues() throws LifeException {
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
//        params.put(Life.KEY_E_STEP_DECREASE, Utils.randomPositiveInteger(bound));
        params.put(Life.KEY_AGE_DEER, rand.nextInt(bound));
        params.put(Life.KEY_AGE_WOLF, rand.nextInt(bound));
        params.put(Life.KEY_I_GRASS, rand.nextInt(bound));
        params.put(Life.KEY_I_DEER, rand.nextInt(bound));
        params.put(Life.KEY_I_WOLF, rand.nextInt(bound));
        params.put(Life.KEY_R_GRASS, rand.nextDouble());
        params.put(Life.KEY_R_DEER, rand.nextDouble());
        params.put(Life.KEY_R_WOLF, rand.nextDouble());

        Life life = new Life(params);
        assertEquals(params.get(Life.KEY_GRID_COLS).intValue(), life.getGridCols());
        assertEquals(params.get(Life.KEY_GRID_ROWS).intValue(), life.getGridRows());
        assertEquals(params.get(Life.KEY_E_GRASS_INITIAL).intValue(), life.E_GRASS_INITIAL);
        assertEquals(params.get(Life.KEY_E_DEER_INITIAL).intValue(), life.E_DEER_INITIAL);
        assertEquals(params.get(Life.KEY_E_WOLF_INITIAL).intValue(), life.E_WOLF_INITIAL);
        assertEquals(params.get(Life.KEY_E_DEER_GAIN).intValue(), life.E_DEER_GAIN);
        assertEquals(params.get(Life.KEY_E_WOLF_GAIN).intValue(), life.E_WOLF_GAIN);
        assertEquals(params.get(Life.KEY_AGE_DEER).intValue(), life.AGE_DEER);
        assertEquals(params.get(Life.KEY_AGE_WOLF).intValue(), life.AGE_WOLF);
        assertEquals(params.get(Life.KEY_I_GRASS).intValue(), life.I_GRASS);
        assertEquals(params.get(Life.KEY_I_DEER).intValue(), life.I_DEER);
        assertEquals(params.get(Life.KEY_I_WOLF).intValue(), life.I_WOLF);
        assertEquals(params.get(Life.KEY_R_DEER).doubleValue(), life.R_DEER, DELTA);
        assertEquals(params.get(Life.KEY_R_WOLF).doubleValue(), life.R_WOLF, DELTA);

        // we don't expect this one to be overridden by our value. Life fixes  R_GRASS
        assertEquals(Life.DEFAULT_R_GRASS, life.R_GRASS, DELTA);
    }

    @Test
    public void testNoMoreDeerWhenNoFoodAndNoReproduction() throws LifeException {
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
    public void testLifeWithInvalidRGrassValueThrowsException() throws LifeException {
        // choose a random double not between 0-1
        double val = randDoubleOutOfRange();

        Map<String, Number> params = new HashMap<String, Number>();
        params.put(Life.KEY_R_DEER, val);
        new Life(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLifeWithInvalidRDEERValueThrowsException() throws LifeException {
        // choose a random double not between 0-1
        double val = randDoubleOutOfRange();

        Map<String, Number> params = new HashMap<String, Number>();
        params.put(Life.KEY_R_DEER, val);
        new Life(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLifeWithInvalidRWolfValueThrowsException() throws LifeException {
        // choose a random double not between 0-1
        double val = randDoubleOutOfRange();

        Map<String, Number> params = new HashMap<String, Number>();
        params.put(Life.KEY_R_WOLF, val);
        new Life(params);
    }

    @Test
    public void testStepReturnsEmptyListWhenNoAgentsToAct() throws LifeException {
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

    @Test
    public void testRun() throws LifeException {
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