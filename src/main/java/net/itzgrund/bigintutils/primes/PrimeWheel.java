package net.itzgrund.bigintutils.primes;

import java.util.Arrays;

public final class PrimeWheel {
    /** Wheel with primes: 2,3,5,7,11,13 */
    public final static PrimeWheel INSTANCE = new PrimeWheel(6);

    /** primes that spans the wheel */
    private final int primes;
    /** index is a odd number: n = (i << 1) + 1 */
    private final short[] steps;

    private PrimeWheel(int num) {
        primes = num;
        int primorial = 1;

        for (int index=0; index<primes; index++) {
            primorial *= ShortPrimes.NUM[index];
        }

        this.steps = new short[primorial >> 1];
        sieveSteps();
    }

    /**
     * Get primorial of the wheel. For example 2*3*5 = 30
     * 
     * @return Primorial of the wheel.
     */
    public int getPrimorial() {
        return this.steps.length << 1;
    }

    /**
     * Get basic primes, that span the wheel.
     * 
     * @return for example new int[] {2,3,5}
     */
    public int[] getPrimes() {
        return Arrays.copyOf(ShortPrimes.NUM, primes);
    }

    /**
     * 
     * @return ShortPrimes.NUM[this.primes]
     */
    public int getNextPrime() {
        return ShortPrimes.NUM[this.primes];
    }

    public short[] getSteps() {
        return this.steps;
    }

    public int[] getBitPattern() {
        int pattern[] = new int[this.steps.length];
        int index = 0;
        int bit = 1;

        do {
            for (int m : steps) {
                if (m > 0) {
                    pattern[index] |= bit;
                }

                bit <<= 1;
                if (bit == 0) {
                    bit = 1;
                    index++;
                }
            }
        } while (index < this.steps.length);

        return pattern;
    }

    private void sieveSteps() {
        int j = 0;

        for (int i = 0; i < this.steps.length; i++) {
            int n = (i << 1) + 1;

            for (int p : getPrimes()) {
                if (n % p == 0) {
                    steps[i] = -1; // no prime
                }
            }

            if (steps[i] == 0) {
                steps[j] = (short)(i - j);
                j = i;
            }
        }

        steps[steps.length - 1] = 1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("Pattern: primorial=");
        builder.append(this.getPrimorial());
        builder.append(" pimes=");
        for (int p : this.getPrimes()) {
            builder.append(" ");
            builder.append(p);
        }
        return builder.toString();
    }
}
