/**
 * Created by Sami on 30/03/2017.
 */

package core;

import core.exceptions.AgentIsDeadException;

public class Deer extends LifeAgent implements Consumes {

    /** @brief default value by which a deer's energy decreases when they age */
    public static final Integer DEFAULT_AGE = 1;

    /** @brief default probability of reproducing */
    public static final Double DEFAULT_R = 0.1;

    /** @brief default initial number deer instances*/
    public static final Integer DEFAULT_I0 = 5;

    /** @brief default initial deer energy */
    public static final Integer DEFAULT_E0 = 5;

    /** @brief default gain value when deers consume consumables */
    public static final Integer DEFAULT_E_GAIN = 2;

    //TODO(sami);
    /** @brief default amount by which a deer's energy is decreased when the deer is consumed */
    public static final Integer DEFAULT_E_LOSS = 10;

    /** @brief decide what the default parameters for this class should be */
    public static LifeAgentOptions getDefaultParams() {
        return new LifeAgentOptions(Deer.class, DEFAULT_AGE, DEFAULT_E0, DEFAULT_R, DEFAULT_I0,
                DEFAULT_E_GAIN, DEFAULT_E_LOSS);
    }

    /** @brief default constructor, calls LifeAgent's constructor */
    public Deer() throws AgentIsDeadException {
        super();
    }

    /**
     * @brief constructor for Deer with @param initial energy
     * @param initialEnergy of the deer
     */
    public Deer(int initialEnergy) throws AgentIsDeadException {
        super(initialEnergy);
    }

    public Deer(Point2D p) throws AgentIsDeadException {
        super(p);
    }

    public Deer(Point2D p, Integer energy) throws AgentIsDeadException {
        super(p, energy);
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        return new Deer(getPos(), this.MY_INITIAL_ENERGY);
    }
}
