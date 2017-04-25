/**
 * Created by Sami on 30/03/2017.
 */

package core;

import core.exceptions.AgentAlreadyDeadException;

/**
 *  Grass is a LifeAgent, it reproduces
 */
public class Grass extends Surface {

    /**  default value by which a deer's energy decreases when they age */
    public static final Integer DEFAULT_AGE = -5;

    /**  default probability of reproducing - all the time */
    public static final Double DEFAULT_R = 1.0;

    /**  default initial number grass instances*/
    public static final Integer DEFAULT_I0 = 5;

    /**  default initial grass energy */
    public static final Integer DEFAULT_E0 = 5;

    /**  default gain value when grass consumes consumables */
    public static final Integer DEFAULT_E_GAIN = 0;

    /**  default amount by which grass's energy is decreased when it is consumed */
    public static final Integer DEFAULT_E_LOSS = 10;

    /**  decide what the default parameters for this class should be */
    public static LifeAgentOptions getDefaultParams() {
        return new LifeAgentOptions(Grass.class, DEFAULT_AGE, DEFAULT_E0, DEFAULT_R, DEFAULT_I0,
                DEFAULT_E_GAIN, DEFAULT_E_LOSS);
    }

    public Grass() throws AgentAlreadyDeadException {
        super();
    }

    public Grass(Integer initialE) throws AgentAlreadyDeadException {
        super(initialE);
    }

    public Grass(Point2D p) throws AgentAlreadyDeadException {
        super(p);
    }

    public Grass(Point2D p, Integer initialE) throws AgentAlreadyDeadException {
        super(p, initialE);
    }
}
