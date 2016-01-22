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

import java.util.Arrays;

/**
 * Prime number generator with Atkins sieve (translated form c source)
 *
 * @author klaus
 * @see http://cr.yp.to/primegen.html
 */
public final class Atkin {

    private final static int PRIMEGEN_WORDS = 1 << 16;
    private final static int B = (PRIMEGEN_WORDS * 32);

    private final int[][] buffer = new int[16][PRIMEGEN_WORDS];
    private final long[] primes; /* p[num-1] ... p[0], in that order */

    private int num = 17;
    private int _pos = PRIMEGEN_WORDS; /* next entry to use in buf; WORDS to restart */

    private long modulo = 60;
    private long L = 1;

    public Atkin() {
        long[] init = new long[] {59, 53, 47, 43, 41, 37, 31, 29, 23, 19, 17, 13, 11, 7, 5, 3, 2};

        this.primes = Arrays.copyOf(init, 512);
    }

    public long next() {
        while (num == 0) {
            fill();
        }

        return primes[--num];
    }

    private void fill() {
        int i;
        int mask;
        int bits0, bits1, bits2, bits3, bits4, bits5, bits6, bits7;
        int bits8, bits9, bits10, bits11, bits12, bits13, bits14, bits15;

        i = _pos;
        if (i == PRIMEGEN_WORDS) {
            sieve();
            L += PRIMEGEN_WORDS * 32;
            i = 0;
        }
        _pos = i + 1;

        bits0 = ~buffer[0][i];
        bits1 = ~buffer[1][i];
        bits2 = ~buffer[2][i];
        bits3 = ~buffer[3][i];
        bits4 = ~buffer[4][i];
        bits5 = ~buffer[5][i];
        bits6 = ~buffer[6][i];
        bits7 = ~buffer[7][i];
        bits8 = ~buffer[8][i];
        bits9 = ~buffer[9][i];
        bits10 = ~buffer[10][i];
        bits11 = ~buffer[11][i];
        bits12 = ~buffer[12][i];
        bits13 = ~buffer[13][i];
        bits14 = ~buffer[14][i];
        bits15 = ~buffer[15][i];

        long base = this.modulo + 1920L;
        this.modulo = base;
        num = 0;

        for (mask = 0x80000000; mask != 0; mask >>>= 1) {
            base -= 60;
            if ((bits15 & mask) != 0) {
                primes[num++] = base + 59;
            }
            if ((bits14 & mask) != 0) {
                primes[num++] = base + 53;
            }
            if ((bits13 & mask) != 0) {
                primes[num++] = base + 49;
            }
            if ((bits12 & mask) != 0) {
                primes[num++] = base + 47;
            }
            if ((bits11 & mask) != 0) {
                primes[num++] = base + 43;
            }
            if ((bits10 & mask) != 0) {
                primes[num++] = base + 41;
            }
            if ((bits9 & mask) != 0) {
                primes[num++] = base + 37;
            }
            if ((bits8 & mask) != 0) {
                primes[num++] = base + 31;
            }
            if ((bits7 & mask) != 0) {
                primes[num++] = base + 29;
            }
            if ((bits6 & mask) != 0) {
                primes[num++] = base + 23;
            }
            if ((bits5 & mask) != 0) {
                primes[num++] = base + 19;
            }
            if ((bits4 & mask) != 0) {
                primes[num++] = base + 17;
            }
            if ((bits3 & mask) != 0) {
                primes[num++] = base + 13;
            }
            if ((bits2 & mask) != 0) {
                primes[num++] = base + 11;
            }
            if ((bits1 & mask) != 0) {
                primes[num++] = base + 7;
            }
            if ((bits0 & mask) != 0) {
                primes[num++] = base + 1;
            }
        }
    }

    private void sieve() {
        int i;
        int[] Lmodqq = new int[49];

        if (L > (long)Integer.MAX_VALUE) {
            for (i = 0; i < 49; ++i) {
                Lmodqq[i] = (int)(L % (long)qqtab[i]);
            }
        } else {
            for (i = 0; i < 49; ++i) {
                Lmodqq[i] = ((int)L) % qqtab[i];
            }
        }

        clear();

        for (i = 0; i < 16; ++i) {
            doit4(buffer[0], for4[i].f, for4[i].g, (long)for4[i].k - L);
        }
        squarefreetiny(buffer[0], Lmodqq, 1);
        for (; i < 32; ++i) {
            doit4(buffer[3], for4[i].f, for4[i].g, (long)for4[i].k - L);
        }
        squarefreetiny(buffer[3], Lmodqq, 13);
        for (; i < 48; ++i) {
            doit4(buffer[4], for4[i].f, for4[i].g, (long)for4[i].k - L);
        }
        squarefreetiny(buffer[4], Lmodqq, 17);
        for (; i < 64; ++i) {
            doit4(buffer[7], for4[i].f, for4[i].g, (long)for4[i].k - L);
        }
        squarefreetiny(buffer[7], Lmodqq, 29);
        for (; i < 80; ++i) {
            doit4(buffer[9], for4[i].f, for4[i].g, (long)for4[i].k - L);
        }
        squarefreetiny(buffer[9], Lmodqq, 37);
        for (; i < 96; ++i) {
            doit4(buffer[10], for4[i].f, for4[i].g, (long)for4[i].k - L);
        }
        squarefreetiny(buffer[10], Lmodqq, 41);
        for (; i < 112; ++i) {
            doit4(buffer[13], for4[i].f, for4[i].g, (long)for4[i].k - L);
        }
        squarefreetiny(buffer[13], Lmodqq, 49);
        for (; i < 128; ++i) {
            doit4(buffer[14], for4[i].f, for4[i].g, (long)for4[i].k - L);
        }
        squarefreetiny(buffer[14], Lmodqq, 53);

        for (i = 0; i < 12; ++i) {
            doit6(buffer[1], for6[i].f, for6[i].g, (long)for6[i].k - L);
        }
        squarefreetiny(buffer[1], Lmodqq, 7);
        for (; i < 24; ++i) {
            doit6(buffer[5], for6[i].f, for6[i].g, (long)for6[i].k - L);
        }
        squarefreetiny(buffer[5], Lmodqq, 19);
        for (; i < 36; ++i) {
            doit6(buffer[8], for6[i].f, for6[i].g, (long)for6[i].k - L);
        }
        squarefreetiny(buffer[8], Lmodqq, 31);
        for (; i < 48; ++i) {
            doit6(buffer[11], for6[i].f, for6[i].g, (long)for6[i].k - L);
        }
        squarefreetiny(buffer[11], Lmodqq, 43);

        for (i = 0; i < 24; ++i) {
            doit12(buffer[2], for12[i].f, for12[i].g, (long)for12[i].k - L);
        }
        squarefreetiny(buffer[2], Lmodqq, 11);
        for (; i < 48; ++i) {
            doit12(buffer[6], for12[i].f, for12[i].g, (long)for12[i].k - L);
        }
        squarefreetiny(buffer[6], Lmodqq, 23);
        for (; i < 72; ++i) {
            doit12(buffer[12], for12[i].f, for12[i].g, (long)for12[i].k - L);
        }
        squarefreetiny(buffer[12], Lmodqq, 47);
        for (; i < 96; ++i) {
            doit12(buffer[15], for12[i].f, for12[i].g, (long)for12[i].k - L);
        }
        squarefreetiny(buffer[15], Lmodqq, 59);

        squarefree49(buffer, L, 247);
        squarefree49(buffer, L, 253);
        squarefree49(buffer, L, 257);
        squarefree49(buffer, L, 263);
        squarefree1(buffer, L, 241);
        squarefree1(buffer, L, 251);
        squarefree1(buffer, L, 259);
        squarefree1(buffer, L, 269);
    }

    private void clear() {
        for (int j = 0; j < 16; ++j) {
            for (int i = 0; i < PRIMEGEN_WORDS; ++i) {
                buffer[j][i] = -1; // 11111111 11111111 11111111 11111111
            }
        }
    }

    private final static int[] two = new int[]{
        0x00000001, 0x00000002, 0x00000004, 0x00000008,
        0x00000010, 0x00000020, 0x00000040, 0x00000080,
        0x00000100, 0x00000200, 0x00000400, 0x00000800,
        0x00001000, 0x00002000, 0x00004000, 0x00008000,
        0x00010000, 0x00020000, 0x00040000, 0x00080000,
        0x00100000, 0x00200000, 0x00400000, 0x00800000,
        0x01000000, 0x02000000, 0x04000000, 0x08000000,
        0x10000000, 0x20000000, 0x40000000, 0x80000000
    };

    private void doit4(int[] a, long x, long y, long start) {
        long i0;
        long y0;
        long i;
        int data;
        int pos;
        int bits;

        x += x;
        x += 15;
        y += 15;

        start += 1000000000;
        while (start < 0) {
            start += x;
            x += 30;
        }
        start -= 1000000000;
        i = start;

        while (i < B) {
            i += x;
            x += 30;
        }

        for (;;) {
            x -= 30;
            if (x <= 15) {
                return;
            }
            i -= x;

            while (i < 0) {
                i += y;
                y += 30;
            }

            i0 = i;
            y0 = y;
            while (i < B) {
                pos = (int)i;
                data = (int)i;
                pos >>>= 5;
                data &= 31;
                i += y;
                y += 30;
                bits = a[pos];
                data = two[data];
                bits ^= data;
                a[pos] = bits;
            }
            i = i0;
            y = y0;
        }
    }

    private void doit6(int[] a, long x, long y, long start) {
        long i0;
        long y0;
        long i;
        int data;
        int pos;
        int bits;

        x += 5;
        y += 15;

        start += 1000000000;
        while (start < 0) {
            start += x;
            x += 10;
        }
        start -= 1000000000;
        i = start;
        while (i < B) {
            i += x;
            x += 10;
        }

        for (;;) {
            x -= 10;
            if (x <= 5) {
                return;
            }
            i -= x;

            while (i < 0) {
                i += y;
                y += 30;
            }

            i0 = i;
            y0 = y;
            while (i < B) {
                pos = (int)i;
                data = (int)i;
                pos >>>= 5;
                data &= 31;
                i += y;
                y += 30;
                bits = a[pos];
                data = two[data];
                bits ^= data;
                a[pos] = bits;
            }
            i = i0;
            y = y0;
        }
    }

    private void doit12(int[] a, long x, long y, long start) {
        long i0;
        long y0;
        long i;
        int data;
        int pos;
        int bits;

        x += 5;

        start += 1000000000;
        while (start < 0) {
            start += x;
            x += 10;
        }
        start -= 1000000000;
        i = start;
        while (i < 0) {
            i += x;
            x += 10;
        }

        y += 15;
        x += 10;

        for (;;) {
            while (i >= B) {
                if (x <= y) {
                    return;
                }
                i -= y;
                y += 30;
            }
            i0 = i;
            y0 = y;
            while ((i >= 0) && (y < x)) {
                pos = (int)i;
                data = (int)i;
                pos >>>= 5;
                data &= 31;
                i -= y;
                y += 30;
                bits = a[pos];
                data = two[data];
                bits ^= data;
                a[pos] = bits;
            }
            i = i0;
            y = y0;
            i += x - 10;
            x += 10;
        }
    }

    private final static int[] deltainverse = new int[] {
        -1, 0,-1,-1,-1,-1,-1, 1,-1,-1,
        -1, 2,-1, 3,-1,-1,-1, 4,-1, 5,
        -1,-1,-1, 6,-1,-1,-1,-1,-1, 7,
        -1, 8,-1,-1,-1,-1,-1, 9,-1,-1,
        -1,10,-1,11,-1,-1,-1,12,-1,13,
        -1,-1,-1,14,-1,-1,-1,-1,-1,15
    };

    private void squarefree1big(int[][] buf, long base, long q, long qq) {
        long i;
        int pos;
        int n;
        long bound = base + 60 * B;

        while (qq < bound) {
            if (bound < Integer.MAX_VALUE) {
                i = qq - (((int)base) % ((int)qq));
            } else {
                i = qq - (base % qq);
            }
            if ((i & 1) == 0) {
                i += qq;
            }

            if (i < B * 60) {
                pos = (int)i;
                n = deltainverse[pos % 60];
                if (n >= 0) {
                    pos /= 60;
                    buf[n][pos >>> 5] |= two[pos & 31];
                }
            }

            qq += q;
            q += 1800;
        }
    }

    private void squarefree1(int[][] buf, long L, int q) {
        int qq;
        int qqhigh;
        int i;
        int ilow;
        int ihigh;
        int n;
        long base;

        base = 60 * L;
        qq = q * q;
        q = 60 * q + 900;

        while (qq < B * 60) {
            if (base < Integer.MAX_VALUE) {
                i = qq - (((int)base) % qq);
            } else {
                i = qq - (int)(base % (long)qq);
            }
            if ((i & 1) == 0) {
                i += qq;
            }

            if (i < B * 60) {
                qqhigh = qq / 60;
                ilow = i % 60;
                ihigh = i / 60;

                qqhigh += qqhigh;
                while (ihigh < B) {
                    n = deltainverse[ilow];
                    if (n >= 0) {
                        buf[n][ihigh >>> 5] |= two[ihigh & 31];
                    }

                    ilow += 2;
                    ihigh += qqhigh;
                    if (ilow >= 60) {
                        ilow -= 60;
                        ihigh += 1;
                    }
                }
            }

            qq += q;
            q += 1800;
        }

        squarefree1big(buf, base, q, qq);
    }

    private void squarefree49big(int[][] buf, long base, int q, long qq) {
        long i;
        int pos;
        int n;
        long bound = base + 60 * B;

        while (qq < bound) {
            if (bound < Integer.MAX_VALUE) {
                i = qq - (((int)base) % ((int)qq));
            } else {
                i = qq - (base % qq);
            }
            if ((i & 1) == 0) {
                i += qq;
            }

            if (i < B * 60) {
                pos = (int)i;
                n = deltainverse[pos % 60];
                if (n >= 0) {
                    pos /= 60;
                    buf[n][pos >>> 5] |= two[pos & 31];
                }
            }

            qq += q;
            q += 1800;
        }
    }

    private void squarefree49(int[][] buf, long L, int q) {
        int qq;
        int qqhigh;
        int i;
        int ilow;
        int ihigh;
        int n;
        long base = 60 * L;

        qq = q * q;
        q = 60 * q + 900;

        while (qq < B * 60) {
            if (base < Integer.MAX_VALUE) {
                i = qq - (((int)base) % qq);
            } else {
                i = qq - (int)(base % (long)qq);
            }
            if ((i & 1) == 0) {
                i += qq;
            }

            if (i < B * 60) {
                qqhigh = qq / 60;
                ilow = i % 60;
                ihigh = i / 60;

                qqhigh += qqhigh;
                qqhigh += 1;
                while (ihigh < B) {
                    n = deltainverse[ilow];
                    if (n >= 0) {
                        buf[n][ihigh >>> 5] |= two[ihigh & 31];
                    }

                    ilow += 38;
                    ihigh += qqhigh;
                    if (ilow >= 60) {
                        ilow -= 60;
                        ihigh += 1;
                    }
                }
            }

            qq += q;
            q += 1800;
        }

        squarefree49big(buf, base, q, qq);
    }

    /* squares of primes >= 7, < 240 */
    private final static int[] qqtab = {
        49, 121, 169, 289, 361, 529, 841, 961, 1369, 1681, 1849, 2209, 2809, 3481, 3721, 4489, 5041,
        5329, 6241, 6889, 7921, 9409, 10201, 10609, 11449, 11881, 12769, 16129, 17161, 18769,
        19321, 22201, 22801, 24649, 26569, 27889, 29929, 32041, 32761, 36481, 37249, 38809,
        39601, 44521, 49729, 51529, 52441, 54289, 57121};

    /* (qq * 11 + 1) / 60 or (qq * 59 + 1) / 60 */
    private final static int[] qq60tab = {
        9, 119, 31, 53, 355, 97, 827, 945, 251, 1653, 339, 405, 515, 3423, 3659, 823, 4957, 977, 6137, 1263, 7789, 1725, 10031, 1945, 2099, 11683, 2341, 2957, 16875, 3441, 18999, 21831, 22421, 4519, 4871, 5113, 5487, 31507, 32215, 35873, 6829, 7115, 38941, 43779, 9117, 9447, 51567, 9953, 56169
    };

    private void squarefreetiny(int[] a, int[] Lmodqq, int d) {
        int j;
        int k;
        int qq;
        int pos;
        int data;
        int bits;

        for (j = 0; j < 49; ++j) {
            qq = qqtab[j];
            k = qq - 1 - ((Lmodqq[j] + qq60tab[j] * d - 1) % qq);
            while (k < B) {
                pos = k;
                data = k;
                pos >>>= 5;
                data &= 31;
                k += qq;
                bits = a[pos];
                data = two[data];
                bits |= data;
                a[pos] = bits;
            }
        }
    }

    private final static class Todo {

        public byte index;
        public byte f;
        public byte g;
        public byte k;

        public Todo(byte index, byte f, byte g, byte k) {
            this.index = index;
            this.f = f;
            this.g = g;
            this.k = k;
        }
    }

    private static Todo todo(int index, int f, int g, int k) {
        return new Todo((byte)index, (byte)f, (byte)g, (byte)k);
    }

    private final static Todo[] for4 = new Todo[]{
        todo( 0, 2,15, 4), todo( 0, 3, 5, 1), todo( 0, 3,25,11), todo( 0, 5, 9, 3),
        todo( 0, 5,21, 9), todo( 0, 7,15, 7), todo( 0, 8,15, 8), todo( 0,10, 9, 8),
        todo( 0,10,21,14), todo( 0,12, 5,10), todo( 0,12,25,20), todo( 0,13,15,15),
        todo( 0,15, 1,15), todo( 0,15,11,17), todo( 0,15,19,21), todo( 0,15,29,29),
        todo( 3, 1, 3, 0), todo( 3, 1,27,12), todo( 3, 4, 3, 1), todo( 3, 4,27,13),
        todo( 3, 6, 7, 3), todo( 3, 6,13, 5), todo( 3, 6,17, 7), todo( 3, 6,23,11),
        todo( 3, 9, 7, 6), todo( 3, 9,13, 8), todo( 3, 9,17,10), todo( 3, 9,23,14),
        todo( 3,11, 3, 8), todo( 3,11,27,20), todo( 3,14, 3,13), todo( 3,14,27,25),
        todo( 4, 2, 1, 0), todo( 4, 2,11, 2), todo( 4, 2,19, 6), todo( 4, 2,29,14),
        todo( 4, 7, 1, 3), todo( 4, 7,11, 5), todo( 4, 7,19, 9), todo( 4, 7,29,17),
        todo( 4, 8, 1, 4), todo( 4, 8,11, 6), todo( 4, 8,19,10), todo( 4, 8,29,18),
        todo( 4,13, 1,11), todo( 4,13,11,13), todo( 4,13,19,17), todo( 4,13,29,25),
        todo( 7, 1, 5, 0), todo( 7, 1,25,10), todo( 7, 4, 5, 1), todo( 7, 4,25,11),
        todo( 7, 5, 7, 2), todo( 7, 5,13, 4), todo( 7, 5,17, 6), todo( 7, 5,23,10),
        todo( 7,10, 7, 7), todo( 7,10,13, 9), todo( 7,10,17,11), todo( 7,10,23,15),
        todo( 7,11, 5, 8), todo( 7,11,25,18), todo( 7,14, 5,13), todo( 7,14,25,23),
        todo( 9, 2, 9, 1), todo( 9, 2,21, 7), todo( 9, 3, 1, 0), todo( 9, 3,11, 2),
        todo( 9, 3,19, 6), todo( 9, 3,29,14), todo( 9, 7, 9, 4), todo( 9, 7,21,10),
        todo( 9, 8, 9, 5), todo( 9, 8,21,11), todo( 9,12, 1, 9), todo( 9,12,11,11),
        todo( 9,12,19,15), todo( 9,12,29,23), todo( 9,13, 9,12), todo( 9,13,21,18),
        todo(10, 2, 5, 0), todo(10, 2,25,10), todo(10, 5, 1, 1), todo(10, 5,11, 3),
        todo(10, 5,19, 7), todo(10, 5,29,15), todo(10, 7, 5, 3), todo(10, 7,25,13),
        todo(10, 8, 5, 4), todo(10, 8,25,14), todo(10,10, 1, 6), todo(10,10,11, 8),
        todo(10,10,19,12), todo(10,10,29,20), todo(10,13, 5,11), todo(10,13,25,21),
        todo(13, 1,15, 3), todo(13, 4,15, 4), todo(13, 5, 3, 1), todo(13, 5,27,13),
        todo(13, 6, 5, 2), todo(13, 6,25,12), todo(13, 9, 5, 5), todo(13, 9,25,15),
        todo(13,10, 3, 6), todo(13,10,27,18), todo(13,11,15,11), todo(13,14,15,16),
        todo(13,15, 7,15), todo(13,15,13,17), todo(13,15,17,19), todo(13,15,23,23),
        todo(14, 1, 7, 0), todo(14, 1,13, 2), todo(14, 1,17, 4), todo(14, 1,23, 8),
        todo(14, 4, 7, 1), todo(14, 4,13, 3), todo(14, 4,17, 5), todo(14, 4,23, 9),
        todo(14,11, 7, 8), todo(14,11,13,10), todo(14,11,17,12), todo(14,11,23,16),
        todo(14,14, 7,13), todo(14,14,13,15), todo(14,14,17,17), todo(14,14,23,21)
    };

    private final static Todo for6[] = {
        todo( 1, 1, 2, 0), todo( 1, 1, 8, 1), todo( 1, 1,22, 8), todo( 1, 1,28,13), todo( 1, 3,10, 2), todo( 1, 3,20, 7),
        todo( 1, 7,10, 4), todo( 1, 7,20, 9), todo( 1, 9, 2, 4), todo( 1, 9, 8, 5), todo( 1, 9,22,12), todo( 1, 9,28,17),
        todo( 5, 1, 4, 0), todo( 5, 1,14, 3), todo( 5, 1,16, 4), todo( 5, 1,26,11), todo( 5, 5, 2, 1), todo( 5, 5, 8, 2),
        todo( 5, 5,22, 9), todo( 5, 5,28,14), todo( 5, 9, 4, 4), todo( 5, 9,14, 7), todo( 5, 9,16, 8), todo( 5, 9,26,15),
        todo( 8, 3, 2, 0), todo( 8, 3, 8, 1), todo( 8, 3,22, 8), todo( 8, 3,28,13), todo( 8, 5, 4, 1), todo( 8, 5,14, 4),
        todo( 8, 5,16, 5), todo( 8, 5,26,12), todo( 8, 7, 2, 2), todo( 8, 7, 8, 3), todo( 8, 7,22,10), todo( 8, 7,28,15),
        todo(11, 1,10, 1), todo(11, 1,20, 6), todo(11, 3, 4, 0), todo(11, 3,14, 3), todo(11, 3,16, 4), todo(11, 3,26,11),
        todo(11, 7, 4, 2), todo(11, 7,14, 5), todo(11, 7,16, 6), todo(11, 7,26,13), todo(11, 9,10, 5), todo(11, 9,20,10)
    };

    private final static Todo for12[] = {
        todo( 2, 2, 1, 0), todo( 2, 2,11,-2), todo( 2, 2,19,-6), todo( 2, 2,29,-14),todo( 2, 3, 4, 0), todo( 2, 3,14,-3),
        todo( 2, 3,16,-4), todo( 2, 3,26,-11),todo( 2, 5, 2, 1), todo( 2, 5, 8, 0), todo( 2, 5,22,-7), todo( 2, 5,28,-12),
        todo( 2, 7, 4, 2), todo( 2, 7,14,-1), todo( 2, 7,16,-2), todo( 2, 7,26,-9), todo( 2, 8, 1, 3), todo( 2, 8,11, 1),
        todo( 2, 8,19,-3), todo( 2, 8,29,-11),todo( 2,10, 7, 4), todo( 2,10,13, 2), todo( 2,10,17, 0), todo( 2,10,23,-4),
        todo( 6, 1,10,-2), todo( 6, 1,20,-7), todo( 6, 2, 7,-1), todo( 6, 2,13,-3), todo( 6, 2,17,-5), todo( 6, 2,23,-9),
        todo( 6, 3, 2, 0), todo( 6, 3, 8,-1), todo( 6, 3,22,-8), todo( 6, 3,28,-13),todo( 6, 4, 5, 0), todo( 6, 4,25,-10),
        todo( 6, 6, 5, 1), todo( 6, 6,25,-9), todo( 6, 7, 2, 2), todo( 6, 7, 8, 1), todo( 6, 7,22,-6), todo( 6, 7,28,-11),
        todo( 6, 8, 7, 2), todo( 6, 8,13, 0), todo( 6, 8,17,-2), todo( 6, 8,23,-6), todo( 6, 9,10, 2), todo( 6, 9,20,-3),
        todo(12, 1, 4,-1), todo(12, 1,14,-4), todo(12, 1,16,-5), todo(12, 1,26,-12),todo(12, 2, 5,-1), todo(12, 2,25,-11),
        todo(12, 3,10,-2), todo(12, 3,20,-7), todo(12, 4, 1, 0), todo(12, 4,11,-2), todo(12, 4,19,-6), todo(12, 4,29,-14),
        todo(12, 6, 1, 1), todo(12, 6,11,-1), todo(12, 6,19,-5), todo(12, 6,29,-13),todo(12, 7,10, 0), todo(12, 7,20,-5),
        todo(12, 8, 5, 2), todo(12, 8,25,-8), todo(12, 9, 4, 3), todo(12, 9,14, 0), todo(12, 9,16,-1), todo(12, 9,26,-8),
        todo(15, 1, 2,-1), todo(15, 1, 8,-2), todo(15, 1,22,-9), todo(15, 1,28,-14),todo(15, 4, 7,-1), todo(15, 4,13,-3),
        todo(15, 4,17,-5), todo(15, 4,23,-9), todo(15, 5, 4, 0), todo(15, 5,14,-3), todo(15, 5,16,-4), todo(15, 5,26,-11),
        todo(15, 6, 7, 0), todo(15, 6,13,-2), todo(15, 6,17,-4), todo(15, 6,23,-8), todo(15, 9, 2, 3), todo(15, 9, 8, 2),
        todo(15, 9,22,-5), todo(15, 9,28,-10),todo(15,10, 1, 4), todo(15,10,11, 2), todo(15,10,19,-2), todo(15,10,29,-10)
    };
}
