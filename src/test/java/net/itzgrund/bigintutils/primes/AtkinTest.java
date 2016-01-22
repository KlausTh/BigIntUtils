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
package net.itzgrund.bigintutils.primes;

import net.itzgrund.bigintutils.Prime;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AtkinTest {
    @Test
    public void shortValues() {
        Atkin atkin = new Atkin();
        
        for (int should : ShortPrimes.NUM) {
            long p = atkin.next();
            assertEquals(p+" != "+should, (long)should, p);
        }
    }
    
    @Test
    public void intValues() {
        Atkin atkin = new Atkin();
        long p1;
        long size = Integer.MAX_VALUE;
        long count = 0;
        do {
            p1 = atkin.next();
            count++;
        } while (p1 < (size));
        
        assertEquals(Prime.PI_2E31,count);
    }
}
