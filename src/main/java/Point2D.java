/**
 * Created by Sami on 30/03/2017.
 */

import java.util.Random;

/**
 * @class Point2D class
 *
 * Point having x and y coordinates
 */
public class Point2D {
    private int x;
    private int y;

    /** @brief
     * @param n bound used in Random for generating x,y positions
     * @return a Point2D instance with random x,y in bounds [0,n]
     */
    public static Point2D randomPoint(int n) {
        Random rand = new Random(System.currentTimeMillis());
        int x = rand.nextInt(n);
        int y = rand.nextInt(n);
        return new Point2D(x, y);
    }

    /** @brief default constructor
     * @param x coordinate
     * @param y coordinate
     */
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
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

    /** @brief copies the field values from the passed Point2D into this one (x,y) */
    public void set(Point2D pos) {
        this.x = pos.getX();
        this.y = pos.getY();
    }

    /** @brief setter for x */
    public void setX(int x) {
        this.x = x;
    }

    /** @brief setter for y */
    public void setY(int y) {
        this.y = y;
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
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

