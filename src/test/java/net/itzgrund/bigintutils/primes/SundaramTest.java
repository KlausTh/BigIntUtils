package net.itzgrund.bigintutils.primes;

import java.util.OptionalInt;
import java.util.stream.IntStream;
import net.itzgrund.bigintutils.Prime;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author klaus
 */
@FixMethodOrder(MethodSorters.DEFAULT)
public class SundaramTest {
    private static Sundaram primes;

    @Test
    public void build() {
        primes = Sundaram.getInstance();
        
        assertNotNull(primes);
    }

    @Test
    public void iterate() {
        OptionalInt max = IntStream.generate(primes.getSupplier()).limit(Prime.PI_2E31).max();

        assertEquals(2147483647, max.getAsInt());
    }
    
    @AfterClass
    public static void cleanup() {
        primes = null;
    }
}
