/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.itzgrund.bigintutils.primes;

/**
 *
 * @author klaus
 */
public final class SegmentSieve {
    private final long base;
    private final long max;
    private Bitarray oddbits;
    private PrimeWheel wheel;
    
    public SegmentSieve(PrimeWheel wheel) {
        this.wheel = wheel;
        this.base = 0L;
        this.max = wheel.getPrimorial();

        sieve();
    }
    
    public SegmentSieve(SegmentSieve pre) {
        this.wheel = pre.wheel;
        this.base = pre.max;
        this.max = pre.max + wheel.getPrimorial();
        
        sieve();
    }

    private void sieve() {
        oddbits = BitarrayFactory.create(wheel.getPrimorial() >> 1);

        initOddBits();

        int i = wheel.getNextPrime() >>> 1;
        int i2 = (i << 1) + 1;
        int ii = (i * i + i) << 1;

        while (ii < this.max) {
            sieveRow(i, i2, ii);

            i = oddbits.nextOne(i);
            i2 = (i << 1) + 1;
            ii = (i * i + i) << 1;
        }
    }
    
    private void initOddBits() {
        oddbits.setPattern(wheel.getBitPattern());
    }

    private void sieveRow(int i, int i2, int ji) {
        short[] steps = wheel.getSteps();
        int step;
        int index = i % steps.length;

        while (ji < max) {
            oddbits.clear(ji);

            step = steps[index];
            ji += step * i2;
            index += step;
            if (index == steps.length) {
                index = 0;
            }
        }
    }
    
    private long nextPrime(long p) {
        return p+2;
    }
}
