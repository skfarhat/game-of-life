package gui;

import core.LifeAgent;
import core.LifeAgentStats;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class StatsPane extends VBox implements LifeStatsObserver {

    private Map<LifeAgentStats, LifeAgentStatsPane> statsToPaneMap = new HashMap<>();

    public StatsPane(Map<Class<?extends LifeAgent>, LifeAgentStats> statsMap, LifeStatsObservable statsObservable) {
        super(10.0); // spacing

        statsObservable.addObserver(this);
        for (Map.Entry<Class<?extends LifeAgent>, LifeAgentStats> e : statsMap.entrySet()) {
            addPane(e.getValue());
        }
    }

    public void addPane(LifeAgentStats stats) {
        LifeAgentStatsPane childPane = new LifeAgentStatsPane(stats);
        statsToPaneMap.put(stats, childPane);
        getChildren().add(childPane);
    }

    public void removePane(LifeAgentStats stats) {
        LifeAgentStatsPane childPane = statsToPaneMap.remove(stats);
        getChildren().remove(childPane);
    }

    @Override
    public void valueAdded(LifeAgentStats s) {
        addPane(s);
    }

    @Override
    public void valueRemoved(LifeAgentStats s) {
        removePane(s);
    }
}
