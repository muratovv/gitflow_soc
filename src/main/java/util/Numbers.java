package util;

/**
 * @author @muratovv
 * @date 05.04.17
 */
public class Numbers {

    public static double EPS = 0.0001;

    /**
     * Less function, with {@link Numbers#EPS} precision
     * <p>
     * Example: le(2., 3.) === 2. < 3.
     */
    public static boolean le(double left, double right) {
        // TODO 05.04.17 Fix epsilon precision
        return left < right;
    }

}
