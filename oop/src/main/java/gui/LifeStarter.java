package gui;

import core.Life;
import core.LifeGetter;
import javafx.beans.property.SimpleIntegerProperty;


enum State {
    STOPPED,
    STARTED,
    PAUSED
};

public interface LifeStarter {

    boolean start(Life life);

    boolean stop();

    boolean pause();

    boolean unpause();

    State getState();

    void setFrequency(double hz);

    LifeGetter lifeGetter();

    SimpleIntegerProperty getIterationsObservable();

}
