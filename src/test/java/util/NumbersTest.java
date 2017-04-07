package util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * @author @muratovv
 * @date 05.04.17
 */
public class NumbersTest {
    private static final Logger logger = LoggerFactory.getLogger(NumbersTest.class);

    @Test
    public void le() throws Exception {
        assertEquals(Numbers.le(2.0, 3.0), true);
        assertEquals(Numbers.le(3.0, 2.0), false);
        assertEquals(Numbers.le(2.0, 2.0 + Numbers.EPS / 2), false);
    }

    @Test
    public void showDouble() throws Exception {
        double d = 3.14;
        logger.info("default: {}", d);
        logger.info("local: {}", String.format(Locale.GERMAN, "%f", d));
    }
}