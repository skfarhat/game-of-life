package gui;

import core.Life;

/**
 * Created by Sami on 05/04/2017.
 */
public interface LifeStarter {
    boolean start(Life life);
    boolean stop();
    boolean pause();

}
