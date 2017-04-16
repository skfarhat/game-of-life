package core;

/**
 * @class LifeAgentParams encapsulates the user configuration parameters describing a type of class
 */
public class LifeAgentParams {

    // ===========================================================================================
    // Defaults
    // ===========================================================================================

    /** @brief default initial energy for LifeAgents*/
    public static final Integer DEFAULT_E0 = 5;

    /** @brief default initial number of instances of that LifeAgent */
    public static final Integer DEFAULT_I0 = 5;

    /** @brief default reproduction rate for LifeAgents */
    public static final Double DEFAULT_R = 0.1;

    /** @brief default energy lost by a LifeAgent when it is consumed - only applicable in some implementations of consume*/
    public static final Integer DEFAULT_E_LOSS = 1;

    /** @brief default energy gained when a LifeAgent consumes another */
    public static final Integer DEFAULT_E_GAIN = 2;

    /** @brief default energy decrease for Agents when they age */
    public static final Integer DEFAULT_AGE = 1;


    // ===========================================================================================
    // Members
    // ===========================================================================================

    private final Class<?extends LifeAgent> agentType;

    /** @brief value by which the LifeAgent ages each step it is chosen */
    private final Integer ageBy;

    /** @brief initial energy of LifeAgents of this type */
    private final Integer initialEnergy;

    /** @brief probability of reproduction of the LifeAgent when it is chosen, value must be in [0;1] */
    private final Double reproductionRate;

    /** @brief the initial number of such agents on start of Life (simulation) */
    private final Integer initialCount;

    /** @brief the energy gained by the LifeAgent when it consumes other LifeAgents - only for implementation 1 */
    private final Integer energyGained;

    /** @bief the energy lost by the LifeAgent when it is consumed by other LifeAgents, value cannot exceed the LifeAgent's own energy */
    private final Integer energyLost;

    /** @brief constructor with all values */
    public LifeAgentParams(Class<? extends LifeAgent> agentType, Integer ageBy, Integer initialEnergy, Double reproductionRate,
                           Integer initialCount, Integer energyGained, Integer energyLost) {

        /* note: ageBy can be negative, as in the case with Grass where the energy increasess at each step */

        Utils.exceptionIfNegative(initialEnergy); // TODO(sami): provide message
        Utils.exceptionIfNegative(initialCount); // TODO(sami): provide message
        Utils.exceptionIfNegative(energyGained);
        Utils.exceptionIfNegative(energyLost);
        Utils.exceptionIfOutOfRange(reproductionRate);

        this.agentType = agentType;
        this.ageBy = ageBy;
        this.initialEnergy = initialEnergy;
        this.reproductionRate = reproductionRate;
        this.initialCount = initialCount;
        this.energyGained = energyGained;
        this.energyLost = energyLost;
    }

    /** @brief constructor with all values */
    public LifeAgentParams(Class<? extends LifeAgent> agentType) {

        /* note: ageBy can be negative, as in the case with Grass where the energy increasess at each step */

        this.agentType = agentType;

        this.ageBy = DEFAULT_AGE;
        Utils.exceptionIfNegative(this.initialEnergy = DEFAULT_E0);
        Utils.exceptionIfNegative(this.initialCount = DEFAULT_I0);
        Utils.exceptionIfNegative(this.energyGained = DEFAULT_E_GAIN);
        Utils.exceptionIfNegative(this.energyLost = DEFAULT_E_LOSS);
        Utils.exceptionIfOutOfRange(this.reproductionRate = DEFAULT_R);
    }

    public Class<? extends LifeAgent> getAgentType() {
        return agentType;
    }

    public Integer getAgeBy() {
        return ageBy;
    }

    public Integer getInitialEnergy() {
        return initialEnergy;
    }

    public Double getReproductionRate() {
        return reproductionRate;
    }

    public Integer getInitialCount() {
        return initialCount;
    }

    public Integer getEnergyGained() {
        return energyGained;
    }

    public Integer getEnergyLost() {
        return energyLost;
    }
}
