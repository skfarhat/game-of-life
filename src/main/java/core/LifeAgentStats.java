package core;

import java.util.Observable;

/**
 *
 */
public class LifeAgentStats extends Observable {

    public static final String NOTIFY_CREATED = "created";
    public static final String NOTIFY_DIED = "died";
    public static final String NOTIFY_REPRODUCED = "reproduced";

    /** the class type of the agent in question. Value is set in the constructor and cannot be changed */
    private final Class<?extends LifeAgent> agentType;

    /** total number of agents of agentType that have been created - counter increments in LifeAgent constructors */
    private long nbCreated = 0;

    /** total number of agents of agentType that have died - counter increments in die() method */
    private long nbDied = 0;

    /** total number of agents of agentType that have reproduced - counter increments in reproduce() method */
    private long nbReproduced = 0;

    public LifeAgentStats(Class<?extends LifeAgent> c) {
        this.agentType = c;
    }

    public void incNbDied() {
        synchronized (this) {
            nbDied++;
            myNotify(NOTIFY_DIED);
        }
    }

    public void incNbCreated() {
        synchronized (this) {
            nbCreated++;
            myNotify(NOTIFY_CREATED);
        }
    }
    public void incNbReproduced() {
        synchronized (this) {
            nbReproduced++;
            myNotify(NOTIFY_REPRODUCED);
        }
    }

    public Class<? extends LifeAgent> getAgentType() {
        return agentType;
    }

    public synchronized long getNbCreated() {
        return nbCreated;
    }

    public synchronized long getNbDied() {
        return nbDied;
    }

    public synchronized long getNbReproduced() {
        return nbReproduced;
    }

    /** to avoid repeating "setChanged(); notifyObservers()" */
    private void myNotify(String str) {
        setChanged();
        notifyObservers(str);
    }

    /**
     * zero all stats
     * method implemented but kept private for the moment awaiting decision on whether to make it public or not
     */
    private void clearStats() {
        nbCreated = 0;
        nbReproduced = 0;
        nbDied = 0;
    }

}
