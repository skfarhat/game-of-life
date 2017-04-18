package core;
import core.exceptions.AgentIsDeadException;

/**
 *
 */
public class Wolf extends LifeAgent {

    /**  default value by which a wolf's energy decreases when they age */
    public static final Integer DEFAULT_AGE = 1;

    /**  default probability of reproducing */
    public static final Double DEFAULT_R = 0.1;

    /**  default initial number wolf instances*/
    public static final Integer DEFAULT_I0 = 5;

    /**  default initial wolf energy */
    public static final Integer DEFAULT_E0 = 10;

    /**  default gain value when wolves consume consumables */
    public static final Integer DEFAULT_E_GAIN = 2;

    /**  default amount by which a wolf's energy is decreased when the wolf is consumed */
    public static final Integer DEFAULT_E_LOSS = 10;


    /**
     * default constructor
     */
    public Wolf() throws AgentIsDeadException {
        super();
    }

    public Wolf(int initialEnergy) throws AgentIsDeadException {
        super(initialEnergy);
    }

    public Wolf(Point2D p) throws AgentIsDeadException {
        super(p);
    }

    public Wolf(Point2D p, Integer energy) throws AgentIsDeadException {
        super(p, energy);
    }

    public LifeAgent reproduce() throws AgentIsDeadException {
        Wolf babyWolf = new Wolf(getPos(), MY_INITIAL_ENERGY);
        return babyWolf;
    }

    public static LifeAgentOptions getDefaultParams() {
        return new LifeAgentOptions(Wolf.class, DEFAULT_AGE, DEFAULT_E0, DEFAULT_R, DEFAULT_I0,
                DEFAULT_E_GAIN, DEFAULT_E_LOSS);
    }
}

