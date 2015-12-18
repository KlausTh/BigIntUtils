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
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * a tool class for primes.
 * 
 * @author klaus@itzgrund.net
 */
public final class Primes {

    public static Stream<BigInteger> getStream() {
        return StreamSupport.stream(getIterator(), false);
    }
    
    private static Spliterator getIterator() {
        return Spliterators.spliterator(getSupplier(), Long.MAX_VALUE,
                Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL
                | Spliterator.ORDERED | Spliterator.SORTED);
    }

    private static Iterator<BigInteger> getSupplier() {
        return new Iterator<BigInteger>() {
            private Atkin sieve = new Atkin();
            private BigInteger value = null;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public BigInteger next() {
                if (value == null) {
                    return getSmall();
                } else {
                    return getBig();
                }
            }

            private BigInteger getSmall() {
                long prime = sieve.next();
                
                if (prime > Integer.MAX_VALUE) {
                    value = BigInteger.valueOf(prime);
                    sieve = null;
                }

                return BigInteger.valueOf(prime);
            }
            
            private BigInteger getBig() {
                value = value.nextProbablePrime();
                
                return value;
            }
        };
    }
}
