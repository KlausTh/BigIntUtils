/*
 Copyright 2015 Klaus Thomas

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package net.itzgrund.bigintutils;

import java.util.OptionalInt;
import java.util.OptionalLong;
import static net.itzgrund.bigintutils.Prime.PI_2E31;
import static net.itzgrund.bigintutils.Prime.PI_2E32;
import net.itzgrund.bigintutils.primes.ShortPrimes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PrimeTest {

    @Test
    public void getShortStream() {
        int[] primes = Prime.getShortStream().limit(ShortPrimes.NUM.length).toArray();
        int i = 0;

        for (int should : ShortPrimes.NUM) {
            assertEquals(primes[i]+" != "+should, should, primes[i++]);
        }
    }
    
    @Test
    public void getIntStream() {
        OptionalInt first = Prime.getIntStream().skip(PI_2E31-1).findFirst();
        
        assertTrue(first.isPresent());
        assertEquals(2147483647, first.getAsInt());
    }

    @Test
    public void getLongStream() {
        OptionalLong first = Prime.getLongStream().skip(PI_2E32).findFirst();
        
        assertTrue(first.isPresent());
        assertEquals(4294967311L, first.getAsLong());
    }
}
