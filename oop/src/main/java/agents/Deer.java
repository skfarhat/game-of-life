package agents;

import core.Creature;
import core.LifeAgentOptions;
import core.Point2D;
import core.exceptions.AgentAlreadyDeadException;

public class Deer extends Creature {

    /**  default value by which a deer's energy decreases when they age */
    public static final Integer DEFAULT_AGE = 1;

    /**  default probability of reproducing */
    public static final Double DEFAULT_R = 0.1;

    /**  default initial number deer instances*/
    public static final Integer DEFAULT_I0 = 5;

    /**  default initial deer energy */
    public static final Integer DEFAULT_E0 = 5;

    /**  default gain value when deers consume consumables */
    public static final Integer DEFAULT_E_GAIN = 2;

    //TODO(sami);
    /**  default amount by which a deer's energy is decreased when the deer is consumed */
    public static final Integer DEFAULT_E_LOSS = 10;

    /**  decide what the default parameters for this class should be */
    public static LifeAgentOptions getDefaultParams() {
        return new LifeAgentOptions(Deer.class, DEFAULT_AGE, DEFAULT_E0, DEFAULT_R, DEFAULT_I0,
                DEFAULT_E_GAIN, DEFAULT_E_LOSS);
    }

    /**  default constructor, calls LifeAgent's constructor */
    public Deer() throws AgentAlreadyDeadException {
        super();
    }

    /**
     *  constructor for agents.Deer with @param initial energy
     * @param initialEnergy of the deer
     */
    public Deer(int initialEnergy) throws AgentAlreadyDeadException {
        super(initialEnergy);
    }

    public Deer(Point2D p) throws AgentAlreadyDeadException {
        super(p);
    }

    public Deer(Point2D p, Integer energy) throws AgentAlreadyDeadException {
        super(p, energy);
    }

}
