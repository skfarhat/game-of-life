package core;

import core.exceptions.LifeImplementationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @class LifeAgentOptions encapsulates the user configuration parameters describing a type of class
 */
public class LifeAgentOptions {

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

    private Class<?extends LifeAgent> agentType;

    /** @brief value by which the LifeAgent ages each step it is chosen */
    private Integer ageBy = DEFAULT_AGE;

    /** @brief initial energy of LifeAgents of this type */
    private Integer initialEnergy = DEFAULT_E0;

    /** @brief probability of reproduction of the LifeAgent when it is chosen, value must be in [0;1] */
    private Double reproductionRate = DEFAULT_R;

    /** @brief the initial number of such agents on start of Life (simulation) */
    private Integer initialCount = DEFAULT_I0;

    /** @brief the energy gained by the LifeAgent when it consumes other LifeAgents - only for implementation 1 */
    private Integer energyGained = DEFAULT_E_GAIN;

    /** @bief the energy lost by the LifeAgent when it is consumed by other LifeAgents, value cannot exceed the LifeAgent's own energy */
    private Integer energyLost = DEFAULT_E_LOSS;

    /** @brief constructor with all values */
    public LifeAgentOptions(Class<? extends LifeAgent> agentType, Integer ageBy, Integer initialEnergy, Double reproductionRate,
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

    /**
     * @brief copy constructor
     * @param opts
     */
    public LifeAgentOptions(LifeAgentOptions opts) {
        copyFrom(opts);
    }

    /** @brief constructor with all values */
    public LifeAgentOptions(Class<? extends LifeAgent> agentType) throws LifeImplementationException {
        try {
            // get the method to be used by reflection
            this.agentType = agentType;
            Method method = agentType.getMethod(LifeAgent.METHOD_NAME_GET_DEFAULT_PARAMS);
            LifeAgentOptions typeParams = (LifeAgentOptions) method.invoke(null);
            if (null != typeParams)
                copyFrom(typeParams);

        } catch (NoSuchMethodException e) {
            // if the static method is not defined for the subclass then we have to get our defaults s
            this.agentType = agentType;
        } catch (IllegalAccessException e) {
            // if we don't have right access to the method invoked (e.g. the method is private)
            e.printStackTrace();
            throw new LifeImplementationException(e.getMessage());
        } catch (InvocationTargetException e) {
            // if the underlying method throws an exception
            e.printStackTrace();
            throw new LifeImplementationException(e.getMessage());
        }
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

    public void setAgeBy(Integer ageBy) {
        this.ageBy = ageBy;
    }

    public void setInitialEnergy(Integer initialEnergy) {
        this.initialEnergy = initialEnergy;
    }

    public void setReproductionRate(Double reproductionRate) {
        this.reproductionRate = reproductionRate;
    }

    public void setInitialCount(Integer initialCount) {
        this.initialCount = initialCount;
    }

    public void setEnergyGained(Integer energyGained) {
        this.energyGained = energyGained;
    }

    public void setEnergyLost(Integer energyLost) {
        this.energyLost = energyLost;
    }

    private void copyFrom(LifeAgentOptions opts) {
        this.agentType = opts.getAgentType();
        this.initialCount = opts.getInitialCount();
        this.ageBy = opts.getAgeBy();
        this.reproductionRate = opts.getReproductionRate();
        this.energyGained = opts.getEnergyGained();
        this.energyLost = opts.getEnergyLost();
        this.initialEnergy = opts.getInitialEnergy();
    }

}
