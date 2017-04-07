package core;

import java.math.BigInteger;
import java.util.Random;


/**
 * Created by Sami on 30/03/2017.
 */

/**
 * @class Utils class contains only static utility methods
 */
public final class Utils {

    /**
     * @class subclass of Random
     * motivation behind using this is to expose the next() method and allow for easy random positive integer generation
     */
    public static class LifeRandom extends Random {

        /** @brief default constructor */
        LifeRandom() { super(); }

        /** @return random string */
        public String randomString() {
            return new BigInteger(130, this).toString(32);
        }

        /** @brief return a random positive integer */
        public Integer randomPositiveInteger() {
            return next(Integer.SIZE - 1);
        }

        public static LifeRandom getRand() {
            return new LifeRandom();
        }
    }

    /** @brief private constructor to prevent instantiation of this class */
    private Utils() {}

    /** @return Random instance */
    public static Random getRand() {
        return LifeRandom.getRand();
    }

    /** @return random positive integer - includes zero */
    public static Integer randomPositiveInteger() {
        return randomPositiveInteger(Integer.MAX_VALUE);
    }

    /** @return random positive integer - includes zero */
    public static Integer randomPositiveInteger(int bound) {
        return new LifeRandom().randomPositiveInteger() % bound;
    }

    /** @return random string */
    public static String randomString() {
        return new LifeRandom().randomString();
    }

    /**
     * @param xBound upper x bound
     * @param yBound upper y bound
     * @return a Point2D instance with random x,y in bounds [0,n]
     */
    public static Point2D randomPoint(int xBound, int yBound) {
        Random rand = LifeRandom.getRand();
        int x = rand.nextInt(xBound);
        int y = rand.nextInt(yBound);
        return new Point2D(x, y);
    }


    /**
     * @param val that must be in 0-1 range
     * @throws IllegalArgumentException if val is out of the range
     */
    public static void exceptionIfOutOfRange(double val) throws IllegalArgumentException {
        if (false == doubleInRange(val))
            throw new IllegalArgumentException("Double values must be between 0 and 1: " + val + " given.");
    }

    /**
     * @param val double to evaluate
     * @return true if the value is in [0,1], false otherwise
     */
    public static boolean doubleInRange(double val) throws IllegalArgumentException {
        if (val < 0 || val > 1)
            return false;
        return true;
    }
}

