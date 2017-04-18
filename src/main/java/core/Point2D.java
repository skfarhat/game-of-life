/**
 * Created by Sami on 30/03/2017.
 */

package core;

import java.util.Random;

/**
 *  Point2D class
 *
 * Point having x and y coordinates
 */
public class Point2D {
    private int x;
    private int y;

    /**  default constructor
     * @param x coordinate
     * @param y coordinate
     */
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**  create by making a copy */
    public Point2D(Point2D p) {
        set(p);
    }

    /**
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**  copies the field values from the passed Point2D into this one (x,y) */
    private void set(Point2D pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Point2D)) return false;
        Point2D p = (Point2D) obj;
        return p.getX() == getX() && p.getY() == getY();
    }

    @Override
    public int hashCode() {
        return x * 31 + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

