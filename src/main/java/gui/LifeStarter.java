package gui;

import core.Life;


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

    double getFrequency();
}
