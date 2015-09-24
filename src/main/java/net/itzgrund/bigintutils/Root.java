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
import java.util.Arrays;

/**
 * ROOT-Funtions for BigInteger
 * 
 * @author klaus@itzgrund.net
 */
public final class Root {

    public final static BigInteger ZERO = BigInteger.ZERO;
    public final static BigInteger ONE = BigInteger.ONE;
    public final static BigInteger TWO = new BigInteger("2");
    public final static BigInteger THREE = new BigInteger("3");
    public final static BigInteger FOUR = new BigInteger("4");
    public final static BigInteger FIVE = new BigInteger("5");
    public final static BigInteger SIX = new BigInteger("6");
    public final static BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

    /**
     * 0 .. 255 filled field of BigIntegers for quick int to BigInteger
     * conversion
     */
    public final static BigInteger[] toBig = new BigInteger[256];

    static {
        toBig[0] = ZERO;
        toBig[1] = ONE;
        toBig[2] = TWO;
        toBig[3] = THREE;
        toBig[4] = FOUR;
        toBig[5] = FIVE;
        toBig[6] = SIX;
        for (int i = 7; i < toBig.length; i++) {
            toBig[i] = BigInteger.valueOf(i);
        }
    }

    /**
     * all possible 8 bits little ending pattern for square numbers
     */
    private final static byte[] SQUARES = new byte[] {
        -127, -124, -119, -112, -111, -103, -95, -92, -87, -79, -71, -63, -60,
        -55, -47, -39, -31, -28, -23, -15, -7, 0, 1, 4, 9, 16, 17, 25, 33, 36,
        41, 49, 57, 64, 65, 68, 73, 81, 89, 97, 100, 105, 113, 121};

    /**
     * Test a number if it is definitly a square number. The method first calls
     * isProbableSquare() and then calls sqrtWithRest().
     * 
     * @param x
     * @return 
     */
    public static boolean isSquare(BigInteger x) {
        return isProbableSquare(x)
                && sqrtWithRest(x)[1].equals(ZERO);
    }

    /**
     * Test a number if it's possible a square. The Method looks for the last
     * byte of x and compares it with BigMath.SQUARES.
     *
     * @param x number to test
     * @return true it may be a square number; false definitely not a sqare number
     * @see SQUARES
     */
    public static boolean isProbableSquare(BigInteger x) {
        byte[] bytes = x.toByteArray();
        byte lastbyte = bytes[bytes.length - 1];
        int index = Arrays.binarySearch(SQUARES, lastbyte);

        return index >= 0;
    }

    /**
     * floor square root
     *
     * @param x positive number
     * @return floor sqare root
     */
    public static BigInteger sqrt(BigInteger x) {
        return sqrtWithRest(x)[0];
    }
    
    /**
     * ceil sqare root
     * 
     * @param x positive number
     * @return ceil sqare root of x
     */
    public static BigInteger sqrtCeil(BigInteger x) {
        BigInteger[] s = sqrtWithRest(x);
        
        if ( ! s[1].equals(BigInteger.ZERO))
            s[0] = s[0].add(BigInteger.ONE);

        return s[0];
    }

    /**
     * floor square root with rest.
     * x = result^2 + rest
     *
     * @param x
     * @return BigInteger[2] { sqrt(x), rest }
     */
    public static BigInteger[] sqrtWithRest(BigInteger x) {
        return sqrtWithRest(x, new BigInteger[2]);
    }

    /**
     * floor square root with rest.
     * x = result^2 + rest
     *
     * @param x
     * @param _return_ field for return
     * @return BigInteger[2] { sqrt(x), rest }
     */
    public static BigInteger[] sqrtWithRest(BigInteger x, BigInteger[] _return_) {
        BigInteger result = ZERO;
        BigInteger rest = ZERO;
        BigInteger resultplusbit;
        int bits;

        for (byte b : x.toByteArray()) {
            int _byte_ = b < 0 ? b + 256 : b;

            rest = rest.shiftLeft(8);
            result = result.shiftLeft(8);

            bits = _byte_ & 192;
            rest = rest.add(toBig[bits]);
            resultplusbit = result.add(toBig[64]);
            result = result.shiftRight(1);

            if (rest.compareTo(resultplusbit) >= 0) {
                rest = rest.subtract(resultplusbit);
                result = result.add(toBig[64]);
            }

            bits = _byte_ & 48;
            rest = rest.add(toBig[bits]);
            resultplusbit = result.add(toBig[16]);
            result = result.shiftRight(1);

            if (rest.compareTo(resultplusbit) >= 0) {
                rest = rest.subtract(resultplusbit);
                result = result.add(toBig[16]);
            }

            bits = _byte_ & 12;
            rest = rest.add(toBig[bits]);
            resultplusbit = result.add(toBig[4]);
            result = result.shiftRight(1);

            if (rest.compareTo(resultplusbit) >= 0) {
                rest = rest.subtract(resultplusbit);
                result = result.add(toBig[4]);
            }

            bits = _byte_ & 3;
            rest = rest.add(toBig[bits]);
            resultplusbit = result.add(toBig[1]);
            result = result.shiftRight(1);

            if (rest.compareTo(resultplusbit) >= 0) {
                rest = rest.subtract(resultplusbit);
                result = result.add(toBig[1]);
            }
        }

        // x = result^2 + rest
        _return_[0] = result;
        _return_[1] = rest;

        return _return_;
    }

    /**
     * floor cubic root
     *
     * @param x
     * @return curtWithRest( x )[ 0 ]
     */
    public static BigInteger curt(BigInteger x) {
        return curtWithRest(x)[0];
    }

    /**
     * floor cubic root with rest
     *
     * @param x
     * @return curtWithRest( x, new BigInteger[ 2 ] )
     */
    public static BigInteger[] curtWithRest(BigInteger x) {
        return curtWithRest(x, new BigInteger[ 2 ]);
    }

    /**
     * floor cubic root with rest
     *
     * @param x
     * @param _return_ returning field
     * @return BigInteger[] { curt( x ), rest }
     */
    public static BigInteger[] curtWithRest(BigInteger x, BigInteger[] _return_) {
        BigInteger rest = BigInteger.ZERO;
        BigInteger yy, y = BigInteger.ZERO;
        BigInteger beta;
        BigInteger bitsieve = BigInteger.valueOf( 16777215 );

        int intervalle = (x.bitLength() - 1) / 24 + 1;

        for (int i = intervalle - 1; i >= 0; i--) {
            rest = rest.shiftLeft(24).add(x.shiftRight(i * 24).and(bitsieve));
            yy = y.multiply(y).multiply(y).shiftLeft(24);
            y = y.shiftLeft(8);

            beta = curtSearchBeta(y, rest, yy);

            rest = rest.subtract(y.add(beta).pow(3).subtract(yy));
            y = y.add(beta);
        }

        // x = y^3 + rest
        _return_[0] = y;
        _return_[1] = rest;

        return _return_;
    }

    private static BigInteger curtSearchBeta(BigInteger y, BigInteger rest, BigInteger yy) {
        BigInteger beta1, beta2 = BigInteger.ZERO;

        beta1 = beta2.add(toBig[128]);
        switch (y.add(beta1).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta1;
            case 1:
                beta1 = beta2;
                break;
        }

        beta2 = beta1.add(toBig[64]);
        switch (y.add(beta2).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta2;
            case 1:
                beta2 = beta1;
                break;
        }

        beta1 = beta2.add(toBig[32]);
        switch (y.add(beta1).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta1;
            case 1:
                beta1 = beta2;
                break;
        }

        beta2 = beta1.add(toBig[16]);
        switch (y.add(beta2).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta2;
            case 1:
                beta2 = beta1;
                break;
        }

        beta1 = beta2.add(toBig[8]);
        switch (y.add(beta1).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta1;
            case 1:
                beta1 = beta2;
                break;
        }

        beta2 = beta1.add(toBig[4]);
        switch (y.add(beta2).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta2;
            case 1:
                beta2 = beta1;
                break;
        }

        beta1 = beta2.add(toBig[2]);
        switch (y.add(beta1).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta1;
            case 1:
                beta1 = beta2;
                break;
        }

        beta2 = beta1.add(BigInteger.ONE);
        if (y.add(beta2).pow(3).subtract(yy).compareTo(rest) <= 0) {
            return beta2;
        } else {
            return beta1;
        }
    }

    /**
     * floor nth root of x
     * 
     * @param nth the degree of root
     * @param x
     * @return nthrootWithRest( nth, x )[0]
     */
    public static BigInteger nthroot(int nth, BigInteger x) {
        return nthrootWithRest(nth, x)[0];
    }

    /**
     * floor nth root of x with rest
     * 
     * @param nth the degree of root
     * @param x
     * @return nthrootWithRest( nth, x, new BigInteger[ 2 ] )
     */
    public static BigInteger[] nthrootWithRest(int nth, BigInteger x) {
        return nthrootWithRest(nth, x, new BigInteger[2]);
    }

    /**
     * floor nth root of x with rest
     * 
     * @param nth the degree of root
     * @param x
     * @param _return_ returning field
     * @return BigInteger[] { nthroot( x ), rest }
     */
    public static BigInteger[] nthrootWithRest(int nth, BigInteger x, BigInteger[] _return_) {
        BigInteger rest = BigInteger.ZERO;
        BigInteger yy, y = BigInteger.ZERO;
        BigInteger alpha, beta;
        BigInteger bitsieb = BigInteger.ONE.shiftLeft(nth * 8).subtract(BigInteger.ONE);

        int intervalle = (x.bitLength() - 1) / (nth * 8) + 1;

        for (int i = intervalle - 1; i >= 0; i--) {
            alpha = x.shiftRight(nth * i * 8).and(bitsieb);
            rest = rest.shiftLeft(nth * 8).add(alpha);
            yy = y.pow(nth).shiftLeft(nth * 8);
            y = y.shiftLeft(8);

            beta = nthrootSearchBeta(nth, y, rest, yy);

            rest = rest.subtract(y.add(beta).pow(nth).subtract(yy));
            y = y.add(beta);
        }

        // x = y^nth + rest
        _return_[0] = y;
        _return_[1] = rest;

        return _return_;
    }

    private static BigInteger nthrootSearchBeta(int nth, BigInteger y, BigInteger rest, BigInteger yy) {
        BigInteger bit = BigInteger.valueOf(128);
        BigInteger beta = BigInteger.ZERO;

        while (!bit.equals(BigInteger.ZERO)) {
            beta = beta.add(bit);

            switch (y.add(beta).pow(nth).subtract(yy).compareTo(rest)) {
                case -1:
                    break;
                case 0:
                    return beta;
                case 1:
                    beta = beta.subtract(bit);
                    break;
            }

            bit = bit.shiftRight(1);
        }

        return beta;
    }
}