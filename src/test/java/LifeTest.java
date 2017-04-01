import org.junit.Test;

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
    public void testLifeDefaults() throws GridCreationException {
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
    public void testLifeWithOverriddenValues() throws GridCreationException {
        Map<String, Number> params = new HashMap<String, Number>();
        Random rand = Utils.getRand();

        params.put(Life.KEY_GRID_N, rand.nextInt(50));
        params.put(Life.KEY_E_GRASS_INITIAL, rand.nextInt());
        params.put(Life.KEY_E_DEER_INITIAL, rand.nextInt());
        params.put(Life.KEY_E_WOLF_INITIAL, rand.nextInt());
        params.put(Life.KEY_E_DEER_GAIN, rand.nextInt());
        params.put(Life.KEY_E_WOLF_GAIN, rand.nextInt());
        params.put(Life.KEY_E_STEP_DECREASE, rand.nextInt());
        params.put(Life.KEY_I_GRASS, rand.nextInt());
        params.put(Life.KEY_I_DEER, rand.nextInt());
        params.put(Life.KEY_I_WOLF, rand.nextInt());
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
    public void testLifeWithInvalidRGrassValueThrowsException() throws GridCreationException {
        // choose a random double not between 0-1
        double val = randDoubleOutOfRange();

        Map<String, Number> params = new HashMap<String, Number>();
        params.put(Life.KEY_R_DEER, val);
        new Life(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLifeWithInvalidRDEERValueThrowsException() throws GridCreationException {
        // choose a random double not between 0-1
        double val = randDoubleOutOfRange();

        Map<String, Number> params = new HashMap<String, Number>();
        params.put(Life.KEY_R_DEER, val);
        new Life(params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLifeWithInvalidRWolfValueThrowsException() throws GridCreationException {
        // choose a random double not between 0-1
        double val = randDoubleOutOfRange();

        Map<String, Number> params = new HashMap<String, Number>();
        params.put(Life.KEY_R_WOLF, val);
        new Life(params);
    }

}