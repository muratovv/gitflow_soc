package util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author @muratovv
 * @date 05.04.17
 */
public class NumbersTest {
    @Test
    public void le() throws Exception {
        assertEquals(Numbers.le(2.0, 3.0), true);
        assertEquals(Numbers.le(3.0, 2.0), false);
        // TODO 05.04.17 FIX tests
        //        assertEquals(Numbers.le(2.0, 2.0 + Numbers.EPS/2), false);
    }

}