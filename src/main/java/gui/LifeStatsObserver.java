package gui;

import core.LifeAgentStats;

public interface LifeStatsObserver {

    void valueAdded(LifeAgentStats s);

    void valueRemoved(LifeAgentStats s);
}
