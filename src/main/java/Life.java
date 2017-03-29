/**
 * Created by Sami on 29/03/2017.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * @class Life class created for each simulation to run. The class captures user input and configures the system parameters.
 *
 * Initially Life was designed to be a singleton class, since intuitively only one life should exist.
 * But it was later noted that we may want to simulate multiple lives at the same time concurrently, which
 * would be impractical (impossible?) with singletons. The design was then changed.
 */
public class Life {

    // ===========================================================================================
    // List of keys in the params map
    // ===========================================================================================

    /** @brief E_GRASS_INITIAL key in params */
    public static final String KEY_E_GRASS_INITIAL = "E_GRASS_INITIAL";

    /** @brief E_DEER_INITIAL key in params */
    public static final String KEY_E_DEER_INITIAL = "E_DEER_INITIAL";

    /** @brief E_WOLF_INITIAL key in params */
    public static final String KEY_E_WOLF_INITIAL = "E_WOLF_INITIAL";

    /** @brief E_DEER_GAIN key in params */
    public static final String KEY_E_DEER_GAIN = "E_DEER_GAIN";

    /** @brief E_WOLF_GAIN key in params */
    public static final String KEY_E_WOLF_GAIN = "E_WOLF_GAIN";

    /** @brief E_STEP_DECREASE key in params */
    public static final String KEY_E_STEP_DECREASE = "E_STEP_DECREASE";

    /** @brief R_GRASS key in params */
    public static final String KEY_R_GRASS = "R_GRASS";

    /** @brief R_DEER key in params */
    public static final String KEY_R_DEER = "R_DEER";

    /** @brief R_WOLF key in params */
    public static final String KEY_R_WOLF = "R_WOLF";

    /** @brief I_GRASS key in params */
    public static final String KEY_I_GRASS = "I_GRASS";

    /** @brief I_DEER key in params */
    public static final String KEY_I_DEER = "I_DEER";

    /** @brief I_WOLF key in params */
    public static final String KEY_I_WOLF = "I_WOLF";

    // ===========================================================================================

    /** @brief default energy gained by wolf and deer when they consume other agents */
    public static final int E_DEFAULT_GAIN = 2;

    /** @brief default energy decrease for Agents at each step */
    public static final int E_DEFAULT_DECREASE = 1;

    /** @brief default initial agent energy */
    public static final int E_DEFAULT_INITIAL = 10;

    /** @brief default random schedule frequency - how often are you likely to be selected to act by the scheduler */
    public static final double R_DEFAULT = 0.33;

    /** @brief default initial number of instance of an agent type */
    public static final int I_DEFAULT = 1;

    /** @brief grass' initial energy */
    public final int E_GRASS_INITIAL;

    /** @brief deer's initial energy */
    public final int E_DEER_INITIAL;

    /** @brief wolf's initial energy */
    public final int E_WOLF_INITIAL;

    /** @brief energy gained when the deer consumes grass */
    public final int E_DEER_GAIN;

    /** @brief energy gained when the wolf consumes a deer */
    public final int E_WOLF_GAIN;

    /** @brief value by which Agent's energy is decreased at each step */
    public final int E_STEP_DECREASE;

    /** @brief grass' random scheduling frequency */
    public final double R_GRASS;

    /** @brief deer's random scheduling frequency */
    public final double R_DEER;

    /** @brief wolf's random scheduling frequency */
    public final double R_WOLF;

    /** @brief initial number of grass instances */
    public final int I_GRASS;

    /** @brief initial number of deers */
    public final int I_DEER;

    /** @brief initial number of wolves */
    public final int I_WOLF;

    /** @brief default constructor, calls other constructor and initialises fields to their defaults */
    Life() throws IllegalArgumentException { this(null);}

    /** @brief constructor taking in a params map specifying the input parameters */
    Life(Map<String, Number> params) throws IllegalArgumentException {
        // if the params map passed is null, we assume the user has no input parameters, we create an empty map
        // and the below code will run and set all fields to their defaults
        if (params == null) params= new HashMap<String, Number>();

        E_GRASS_INITIAL = params.containsKey(KEY_E_GRASS_INITIAL)? params.get(KEY_E_GRASS_INITIAL).intValue() : E_DEFAULT_INITIAL;
        E_DEER_INITIAL = params.containsKey(KEY_E_DEER_INITIAL)? params.get(KEY_E_DEER_INITIAL).intValue() : E_DEFAULT_INITIAL;
        E_WOLF_INITIAL = params.containsKey(KEY_E_WOLF_INITIAL)? params.get(KEY_E_WOLF_INITIAL).intValue() : E_DEFAULT_INITIAL;
        E_DEER_GAIN = params.containsKey(KEY_E_DEER_GAIN)? params.get(KEY_E_DEER_GAIN).intValue() : E_DEFAULT_GAIN;
        E_WOLF_GAIN = params.containsKey(KEY_E_WOLF_GAIN)? params.get(KEY_E_WOLF_GAIN).intValue() : E_DEFAULT_GAIN;
        E_STEP_DECREASE = params.containsKey(KEY_E_STEP_DECREASE)? params.get(KEY_E_STEP_DECREASE).intValue() : E_DEFAULT_DECREASE;
        I_GRASS = params.containsKey(KEY_I_GRASS)? params.get(KEY_I_GRASS).intValue() : I_DEFAULT;
        I_DEER = params.containsKey(KEY_I_DEER)? params.get(KEY_I_DEER).intValue() : I_DEFAULT;
        I_WOLF = params.containsKey(KEY_I_WOLF)? params.get(KEY_I_WOLF).intValue() : I_DEFAULT;

        // the doubles must be in range 0-1
        checkInRange(R_GRASS = params.containsKey(KEY_R_GRASS)? params.get(KEY_R_GRASS).doubleValue() : R_DEFAULT);
        checkInRange(R_DEER = params.containsKey(KEY_R_DEER)? params.get(KEY_R_DEER).doubleValue() : R_DEFAULT);
        checkInRange(R_WOLF = params.containsKey(KEY_R_WOLF)? params.get(KEY_R_WOLF).doubleValue() : R_DEFAULT);
    }

    /** @brief checks that the passed double is in the range 0-1 */
    private void checkInRange(double val) throws IllegalArgumentException {
        if (val < 0 || val > 1)
            throw new IllegalArgumentException("Double values must be between 0 and 1: " + val + " given.");
    }

    /** @brief run this intolerable thing that is called life */
    public void run() {

    }

    public static void main(String []args) {
        Life life = new Life();
        life.run();
    }
}
