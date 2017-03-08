package util;

/**
 * @author @muratovv
 * @date 08.03.17
 */
public class Strings {
    /**
     * Wrap string with quotes
     *
     * @param str source string
     *
     * @return quoted sting
     */
    public static String wrapWithQuotes(String str) {
        return "\"".concat(str).concat("\"");
    }
}
