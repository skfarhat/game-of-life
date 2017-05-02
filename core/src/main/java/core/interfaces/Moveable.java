package core.interfaces;

import core.Point2D;

public interface Moveable {

    /** get current position */
    Point2D getPos();

    /**  move to new position */
    void moveTo(Point2D pos);
}
