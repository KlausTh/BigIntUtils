/*
 Copyright 2016 Klaus Thomas

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
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import net.itzgrund.bigintutils.primes.Atkin;
import net.itzgrund.bigintutils.primes.ShortPrimes;

/**
 * a tool class for primes.
 * 
 * @author klaus@itzgrund.net
 */
public final class Prime {
    public final static long PI_2E07 = 31L;
    public final static long PI_2E08 = 54L;
    public final static long PI_2E15 = 3512L;
    public final static long PI_2E16 = 6542L;
    public final static long PI_2E31 = 105097565L;
    public final static long PI_2E32 = 203280221L;
    public final static long PI_2E63 = 216289611853439384L;
    public final static long PI_2E64 = 425656284035217743L;

    /** @return The first 6542 prime numbers */
    public static IntStream getShortStream() {
        return IntStream.range(0, ShortPrimes.NUM.length)
                .map(i -> ShortPrimes.NUM[i]);
    }

    /** @return The first 105097565 prime numbers */
    public static IntStream getIntStream() {
        Atkin atkin = new Atkin();

        return IntStream.generate(() -> (int)atkin.next()).limit(PI_2E31);
    }

    /** @return The first 216289611853439384 prime numbers */
    public static LongStream getLongStream() {
        Atkin atkin = new Atkin();

        return LongStream.generate(() -> atkin.next()).limit(PI_2E63);
    }
    
    /** @return All prime numbers in a infinite Stream */
    public static Stream<BigInteger> getStream() {
        Stream<BigInteger> first = getLongStream().mapToObj(l -> BigInteger.valueOf(l));
        Stream<BigInteger> second = getBigStream();
        
        return Stream.concat(first, second);
    }

    /**
     * TODO: implement a faster method (wheel -> prime test)
     * 
     * @return primes > Long.MAX_VALUE
     */
    protected static Stream<BigInteger> getBigStream() {
        return Stream.iterate(
                BigInteger.valueOf(Long.MAX_VALUE).nextProbablePrime(),
                p -> p.nextProbablePrime());
    }
}
