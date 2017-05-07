package gui;

import core.Life;
import core.LifeGetter;
import javafx.beans.property.SimpleIntegerProperty;

public interface LifeStarter {

    boolean start(Life life);

    boolean stop();

    boolean pause();

    boolean unpause();

    LifeState getState();

    void setFrequency(double hz);

    LifeGetter lifeGetter();

    SimpleIntegerProperty getIterationsObservable();

}
