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

        /** @brief seeded constructor */
        LifeRandom(long seed)  { super(seed); }

        /** @return random string */
        public String randomString() {
            return new BigInteger(130, this).toString(32);
        }

        /** @brief return a random positive integer */
        public Integer randomPositiveInteger() {
            return next(Integer.SIZE - 1);
        }

        public static LifeRandom getRand() {
            return new LifeRandom(System.currentTimeMillis());
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
        return new LifeRandom().randomPositiveInteger();
    }

    /** @return random string */
    public static String randomString() {
        return new LifeRandom().randomString();
    }

}
