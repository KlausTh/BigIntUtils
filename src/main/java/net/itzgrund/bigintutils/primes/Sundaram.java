package net.itzgrund.bigintutils.primes;

import java.util.NoSuchElementException;
import java.util.function.IntSupplier;

public final class Sundaram {
    private static Sundaram singleton = null;
    private static final int MAX = 1 << 30;

    private Bitarray oddbits;

    private Sundaram() {
    }

    public static Sundaram getInstance() {
        if (singleton == null) {
            createInstance();
        }
        return singleton;
    }

    private static synchronized void createInstance() {
        if (singleton == null) {
            singleton = new Sundaram();
            singleton.sieve();
        }
    }

    private Bitarray sieve() {
        PrimeWheel wheel = PrimeWheel.INSTANCE;
        oddbits = BitarrayFactory.create(MAX);

        initPrimeWheel(wheel);

        int i = wheel.getNextPrime() >>> 1;
        int i2 = (i << 1) + 1;
        int ii = (i * i + i) << 1;

        while (ii < MAX) {
            sieveRow(wheel, i, i2, ii);

            i = oddbits.nextOne(i);
            i2 = (i << 1) + 1;
            ii = (i * i + i) << 1;
        }

        return oddbits;
    }

    private void initPrimeWheel(PrimeWheel wheel) {
        oddbits.setPattern(wheel.getBitPattern());

        oddbits.clear(0);// <- 1 no prime
        for (int p : wheel.getPrimes()) {
            oddbits.set(p >>> 1);
        }
    }

    private void sieveRow(PrimeWheel wheel, int i, int i2, int ji) {
        short[] steps = wheel.getSteps();
        int step;
        int index = i % steps.length;

        while (ji < MAX) {
            oddbits.clear(ji);

            step = steps[index];
            ji += step * i2;
            index += step;
            if (index == steps.length) {
                index = 0;
            }
        }
    }

    public boolean isPrime(int n) {
        int _n = n >>> 1;

        if (n == 2) {
            return true;
        }
        if (((int)n & 1) == 0) // even number?
        {
            return false;
        }

        return isPrimeOdd(_n);
    }

    private boolean isPrimeOdd(int n) {
        return this.oddbits.isOne(n);
    }

    public IntSupplier getSupplier() {
        return new IntSupplier() {
            int[] buffer = new int[256];
            int len = 0;
            int i = -1;
            int last;

            @Override
            public int getAsInt() {
                if (i == -1) {
                    i = 0;
                    len = oddbits.getIndicesArray(buffer, i);
                    last = buffer[len-1];
                    return 2;
                }

                if (i == len) {
                    i = 0;
                    len = oddbits.getIndicesArray(buffer, last);
                    if (len == 0) {
                        throw new NoSuchElementException();
                    }
                    last = buffer[len-1];
                }

                return (buffer[i++] << 1) + 1;
            }
        };
    }

    @Override
    public String toString() {
        return "max = " + (2L * this.oddbits.size()) + ":" + this.oddbits.toString();
    }
}
