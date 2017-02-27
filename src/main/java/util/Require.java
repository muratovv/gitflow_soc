package util;

/**
 * @author @muratovv
 * @date 27.02.17
 */
public class Require {
    public static void require(Condition cond, String message) {
        if (!cond.test())
            throw new ViolatedRequire(message);
    }

    public interface Condition {
        boolean test();
    }

    public static class ViolatedRequire extends RuntimeException {
        public ViolatedRequire(String message) {
            super(message);
        }
    }
}
