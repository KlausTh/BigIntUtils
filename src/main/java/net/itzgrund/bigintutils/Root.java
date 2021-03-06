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
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * ROOT-Funtions for BigInteger
 * 
 * @author klaus@itzgrund.net
 * @see https://en.wikipedia.org/wiki/Shifting_nth_root_algorithm
 */
public final class Root {
    private final static BigInteger MASK_24BIT = new BigInteger("16777215");

    /**
     * 0 .. 255 filled field of BigIntegers for quick int to BigInteger
     * conversion
     */
    final static BigInteger[] intToBig = new BigInteger[256];

    static {
        intToBig[0] = ZERO;
        intToBig[1] = ONE;
        for (int i = 2; i < intToBig.length; i++) {
            intToBig[i] = BigInteger.valueOf(i);
        }
    }

    /**
     * Test a number if it is definitly a square number. The method first calls
     * isProbableSquare() and then calls sqrtWithRest().
     * 
     * @param x the number to test.
     * @return true: x is a square number
     */
    public static boolean isSquare(BigInteger x) {
        return isProbableSquare(x)
                && sqrtWithRest(x)[1].equals(ZERO);
    }

    /**
     * Test a number if it's possible a square. The Method looks for the last
     * 2 bits of x.
     *
     * @param x number to test
     * @return true it may be a square number; false definitely not a sqare number
     * @see SQUARES
     */
    public static boolean isProbableSquare(BigInteger x) {
        byte[] bytes = x.toByteArray();
        
        return isProbablySquare(bytes, bytes.length - 1);
    }

    private static boolean isProbablySquare(byte[] bytes, int index) {
        byte bits = bytes[index];

        switch (bits & 0b11) {
        case 0b01: // check 101?
            return (bits & 0b101) != 0b101;
        case 0b10:
        case 0b11:
            return false;
        }

        switch (bits & 0b1100) {
        case 0b0100: // 10100?
            return (bits & 0b10100) != 0b10100;
        case 0b1000:
        case 0b1100:
            return false;
        }

        switch (bits & 0b110000) {
        case 0b010000: // 1010000?
            return (bits & 0b1010000) != 0b1010000;
        case 0b100000:
        case 0b110000:
            return false;
        }

        switch (bits & 0b11000000) {
        case 0b01000000: // 1 01000000?
            return (index == 0) || ((bytes[index-1] & 1) != 1);
        case 0b10000000:
        case 0b11000000:
            return false;
        }

        if (index > 0)
            return isProbablySquare(bytes, index - 1);
        else
            return true;
    }

    /**
     * Test a number if it's possible a cubic number. The Method looks for the last
     * 3 bits of x.
     *
     * @param x number to test
     * @return true it may be a square number; false definitely not a sqare number
     * @see SQUARES
     */
    public static boolean isProbableCubic(BigInteger x) {
        byte[] bytes = x.toByteArray();
        
        return isProbableCubic(bytes, bytes.length-1);
    }
    
    private static boolean isProbableCubic(byte[] bytes, int index) {
        short bits = (short)bytes[index];

        switch (bits & 0b00000111) {
            case 0b00000010:
            case 0b00000100:
            case 0b00000110:
                return false;
            case 0b00000000:
                break;
            default:
                return true;
        }
        
        switch (bits & 0b00111000) {
            case 0b00010000:
            case 0b00100000:
            case 0b00110000:
                return false;
            case 0b00000000:
                break;
            default:
                return true;
        }
        
        if (index--==0)
            return true;

        bits |= ((short)bytes[index]) << 8;
        bits >>>= 6; // 11111100
        
        switch (bits & 0b00000111) {
            case 0b00000010:
            case 0b00000100:
            case 0b00000110:
                return false;
            case 0b00000000:
                break;
            default:
                return true;
        }
        
        switch (bits & 0b00111000) {
            case 0b00010000:
            case 0b00100000:
            case 0b00110000:
                return false;
            case 0b00000000:
                break;
            default:
                return true;
        }
        
        if (index--==0)
            return true;

        bits >>>= 2;
        bits |= ((short)bytes[index]) << 8;
        bits >>>= 4; // 22221111
        
        switch (bits & 0b00000111) {
            case 0b00000010:
            case 0b00000100:
            case 0b00000110:
                return false;
            case 0b00000000:
                break;
            default:
                return true;
        }
        
        switch (bits & 0b00111000) {
            case 0b00010000:
            case 0b00100000:
            case 0b00110000:
                return false;
            case 0b00000000:
                break;
            default:
                return true;
        }
        
        if (index--==0)
            return true;

        return isProbableCubic(bytes, index);
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
            rest = rest.add(intToBig[bits]);
            resultplusbit = result.add(intToBig[64]);
            result = result.shiftRight(1);

            if (rest.compareTo(resultplusbit) >= 0) {
                rest = rest.subtract(resultplusbit);
                result = result.add(intToBig[64]);
            }

            bits = _byte_ & 48;
            rest = rest.add(intToBig[bits]);
            resultplusbit = result.add(intToBig[16]);
            result = result.shiftRight(1);

            if (rest.compareTo(resultplusbit) >= 0) {
                rest = rest.subtract(resultplusbit);
                result = result.add(intToBig[16]);
            }

            bits = _byte_ & 12;
            rest = rest.add(intToBig[bits]);
            resultplusbit = result.add(intToBig[4]);
            result = result.shiftRight(1);

            if (rest.compareTo(resultplusbit) >= 0) {
                rest = rest.subtract(resultplusbit);
                result = result.add(intToBig[4]);
            }

            bits = _byte_ & 3;
            rest = rest.add(intToBig[bits]);
            resultplusbit = result.add(intToBig[1]);
            result = result.shiftRight(1);

            if (rest.compareTo(resultplusbit) >= 0) {
                rest = rest.subtract(resultplusbit);
                result = result.add(intToBig[1]);
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

        int intervals = (x.bitLength() - 1) / 24 + 1;

        for (int i = intervals - 1; i >= 0; i--) {
            rest = rest.shiftLeft(24).add(x.shiftRight(i * 24).and(MASK_24BIT));
            yy = y.pow(3).shiftLeft(24);
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

        beta1 = beta2.add(intToBig[128]);
        switch (y.add(beta1).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta1;
            case 1:
                beta1 = beta2;
                break;
        }

        beta2 = beta1.add(intToBig[64]);
        switch (y.add(beta2).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta2;
            case 1:
                beta2 = beta1;
                break;
        }

        beta1 = beta2.add(intToBig[32]);
        switch (y.add(beta1).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta1;
            case 1:
                beta1 = beta2;
                break;
        }

        beta2 = beta1.add(intToBig[16]);
        switch (y.add(beta2).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta2;
            case 1:
                beta2 = beta1;
                break;
        }

        beta1 = beta2.add(intToBig[8]);
        switch (y.add(beta1).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta1;
            case 1:
                beta1 = beta2;
                break;
        }

        beta2 = beta1.add(intToBig[4]);
        switch (y.add(beta2).pow(3).subtract(yy).compareTo(rest)) {
            case 0:
                return beta2;
            case 1:
                beta2 = beta1;
                break;
        }

        beta1 = beta2.add(intToBig[2]);
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
        BigInteger bitsieve = BigInteger.ONE.shiftLeft(nth * 8).subtract(BigInteger.ONE);

        int intervalle = (x.bitLength() - 1) / (nth * 8) + 1;

        for (int i = intervalle - 1; i >= 0; i--) {
            alpha = x.shiftRight(nth * i * 8).and(bitsieve);
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
        BigInteger bit = intToBig[128];
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
