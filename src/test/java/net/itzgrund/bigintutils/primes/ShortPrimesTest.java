package net.itzgrund.bigintutils.primes;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author klaus
 */
public class ShortPrimesTest {
    @Test
    public void length() {
        assertEquals(ShortPrimes.MAX, ShortPrimes.NUM[ShortPrimes.NUM.length-1]);
    }
}
