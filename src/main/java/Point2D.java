/**
 * Created by Sami on 30/03/2017.
 */

/**
 * @class Point2D class
 *
 * Point having x and y coordinates
 */
public class Point2D {
    private int x;
    private int y;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Point2D)) return false;
        Point2D p = (Point2D) obj;
        return p.getX() == getX() && p.getY() == getY();
    }
}

