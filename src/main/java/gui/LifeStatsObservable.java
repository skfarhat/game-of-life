package gui;

public interface LifeStatsObservable {

    void addObserver(LifeStatsObserver o);

    void removeObserver(LifeStatsObserver o);
}
