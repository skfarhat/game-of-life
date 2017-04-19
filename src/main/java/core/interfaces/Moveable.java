package core.interfaces;

import core.Point2D;

/**
 * Created by Sami on 31/03/2017.
 */
public interface Moveable {

    /** get current position */
    Point2D getPos();

    /**  move to new position */
    void moveTo(Point2D pos);
}
