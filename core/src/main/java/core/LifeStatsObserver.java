package core;

public interface LifeStatsObserver {

    void valueAdded(LifeAgentStats s);

    void valueRemoved(LifeAgentStats s);
}
