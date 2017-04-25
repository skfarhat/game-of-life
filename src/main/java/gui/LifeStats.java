package gui;

import core.LifeAgent;
import core.LifeAgentStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// NOTE: observe pattern only works for single put(), remove()
// and is not tested for putAll()

public class LifeStats extends HashMap<Class<?extends LifeAgent>, LifeAgentStats> implements LifeStatsObservable {

    private List<LifeStatsObserver> lifeStatsObservers = new ArrayList();

    @Override
    public LifeAgentStats put(Class<? extends LifeAgent> key, LifeAgentStats value) {
        super.put(key, value);
        try {
            addNotify(value);
        }
        catch(RuntimeException re) {
            re.printStackTrace();
        }
        return value;
    }

    public void addNotify(LifeAgentStats s) {
        for (LifeStatsObserver o : lifeStatsObservers)
            o.valueAdded(s);
    }

    public void removeNotify(LifeAgentStats s) {
        for (LifeStatsObserver o : lifeStatsObservers)
            o.valueRemoved(s);
    }

    @Override
    public void addObserver(LifeStatsObserver o) {
        lifeStatsObservers.add(o);
    }

    @Override
    public void removeObserver(LifeStatsObserver o) {
        lifeStatsObservers.remove(o);
    }
}
