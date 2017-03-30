import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Sami on 30/03/2017.
 */
public final class Utils {

    /** @brief private constructor to prevent instantiation of this class */
    private Utils() {}

    /** @return random string */
    public static String randomString() {
        Random rand = new Random(System.currentTimeMillis());
        return new BigInteger(130, rand).toString(32);
    }

}
