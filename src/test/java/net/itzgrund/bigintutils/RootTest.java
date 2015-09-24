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

import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RootTest {

    private final BigInteger n = new BigInteger("1234567890").shiftLeft(5000);
    private final long[] cases = new long[]{ 1, 2, 3, 4, 5, 10, 555, 12345, 9876543210l, Long.MAX_VALUE };

    @Test
    public void isProbableSquare() {
        for ( BigInteger bn : Root.toBig ) {
            String msg = bn.toString() + "ist eine Quadratzahl";
            assertTrue( msg, Root.isProbableSquare( bn.multiply( bn ) ) );
        }
        for ( int i : new int[] { 3,5,6,7,8,10,11,12,13,14,15,18,19,20,21 } ) {
            String msg = i + "ist keine Quadratzahl";
            assertFalse( msg, Root.isProbableSquare( Root.toBig[ i ] ) );
        }
    }

    @Test
    public void isSquare() {
        for ( BigInteger bn : Root.toBig ) {
            String msg = bn.toString() + "ist eine Quadratzahl";
            assertTrue( msg, Root.isSquare( bn.multiply( bn ) ) );
        }
        for ( int i : new int[] { 2,3,5,6,7,8,10,11,12,13,14,15,17,18,19,20,21 } ) {
            String msg = i + "ist keine Quadratzahl";
            assertFalse( msg, Root.isSquare( Root.toBig[ i ] ) );
        }
    }

    @Test
    public void sqrt() {
        for (long i : cases) {
            BigInteger bi = BigInteger.valueOf(i);
            BigInteger bii = bi.multiply(bi);
            assertEquals("sqrt(" + bii + ") = ", bi, Root.sqrt(bii));
            bi = bi.subtract(BigInteger.ONE);
            bii = bii.subtract(BigInteger.ONE);
            assertEquals("sqrt(" + bii + ") = ", bi, Root.sqrt(bii));
        }
        assertEquals("sqrt(" + n + ") = ", n, Root.sqrt(n.pow(2)));
        
        assertEquals( new BigInteger( "16304" ), Root.sqrt( new BigInteger( "265847249" ) ) );
    }

    @Test
    public void curt() {
        for (long i : cases) {
            BigInteger bi = BigInteger.valueOf(i);
            BigInteger bii = bi.multiply(bi).multiply(bi);
            assertEquals("curt(" + bii + ") = ", bi, Root.curt(bii));
            bi = bi.subtract(BigInteger.ONE);
            bii = bii.subtract(BigInteger.ONE);
            assertEquals("curt(" + bii + ") = ", bi, Root.curt(bii));
        }
        assertEquals("curt(" + n + ") = ", n, Root.curt(n.pow(3)));
    }

    @Test
    public void nthroot() {
        for (long i : cases) {
            for (int r = 1; r < 6; r++) {
                BigInteger bi = BigInteger.valueOf(i);
                BigInteger bii = bi.pow(r);
                assertEquals("nthroot(" + r + ", " + bii + ") = ", bi, Root.nthroot(r, bii));
                bi = bi.subtract(BigInteger.ONE);
                bii = bii.subtract(BigInteger.ONE);
                assertEquals("nthroot(" + r + ", " + bii + ") = ", bi, Root.nthroot(r, bii));
            }
        }
        assertEquals("nthroot(4," + n + ") = ", n, Root.nthroot(4, n.pow(4)));
    }
}
