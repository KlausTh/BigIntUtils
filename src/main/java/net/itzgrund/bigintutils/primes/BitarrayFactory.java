package net.itzgrund.bigintutils.primes;

import java.io.Serializable;
import java.util.Arrays;

public abstract class BitarrayFactory<T extends Number> implements Bitarray {
	protected final int length;

	public static Bitarray create(int length) {
		if (length < 1L)
			throw new IllegalArgumentException("minimum length is 1");

		if (System.getProperty("sun.arch.data.model", "32").equals("64")) {
			return new Bitarray64(length);
		} else {
			return new Bitarray32(length);
		}
	}
	
	protected BitarrayFactory(int length) {
		this.length = length;
	}

	/** number of bits in field */
	@Override
	public final int size() {
		return this.length;
	}

	private static final class Bitarray32 extends BitarrayFactory<Integer> implements Serializable {
		private static final long serialVersionUID = -721833446576568558L;
		private static final int OFFSET = 5;
		private static final int BITS = 1 << OFFSET;
		private static final int MASK = BITS - 1;
		private final int[] field;

		protected Bitarray32(int length) {
			super(length);
			int len = length >>> OFFSET;
			int offset = length & MASK;

			if (offset > 0)
				len++;

			this.field = new int[len];
		}

		/** is the bit set to 1? */
		@Override
		public boolean isOne(int index) {
			int i = index >>> OFFSET;
			int o = index & MASK;

			return (this.field[i] & (1 << o)) != 0 ? true : false;
		}

		@Override
		public void clear(int index) {
			int i = index >>> OFFSET;
			int o = index & MASK;

			this.field[i] &= ~(1 << o);
		}

		@Override
		public void set(int index) {
			int i = index >>> OFFSET;
			int o = index & MASK;

			this.field[i] |= (1 << o);
		}
		
		@Override
		public void flip(int index) {
			int i = index >>> OFFSET;
			int o = index & MASK;

			this.field[i] ^= (1 << o);
		}

		@Override
		public int nextOne(int index) {
			int i = index >>> OFFSET;
			int bit = 1 << (index & MASK);
			int word = this.field[i];

			do {
				index++;
				bit <<= 1;

				if (bit == 0L) {
					bit = 1;
					if (++i == this.field.length)
						return -1;
					word = this.field[i];
				}
			} while ((word & bit) == 0);

			return index;
		}

		/** number of bits that are set to 1 */
		@Override
		public int count() {
			int count = 0;

			for (int i=0; i<field.length; i++) {
				count += Integer.bitCount(field[i]);
			}

			return count;
		}

		/** Set all bits to zero */
		@Override
		public void reset() {
			Arrays.fill(this.field, 0, this.field.length, 0);
		}
		
		/** Set all bits to one */
		@Override
		public void preset() {
			Arrays.fill(this.field, 0, this.field.length, -1);

			// set unused bits to zero
			int o = length >>> OFFSET;
			if (o > 0)
				field[field.length-1] &= (-1 >>> (BITS - o));
		}
		
		@Override
		public void setPattern(int[] pattern) {
			int i = 0;
			int j = 0;
			
			while(j < this.field.length) {
				this.field[j++] = pattern[i++];
				if (i == pattern.length) {
					i = 0;
				}
			}
		}

                @Override
                public int getIndicesArray(int[] array, int after) {
                    int index = 0;
                    int i = after >>> OFFSET;
                    int bit = 1 << (after & MASK);
                    int word = this.field[i];

                    while (index < array.length) {
                        after++;
                        bit <<= 1;

                        if (bit == 0) {
                            bit = 1;
                            if (++i == this.field.length) {
                                return index;
                            }
                            word = this.field[i];
                        }

                        if ((word & bit) != 0L) {
                            array[index++] = after;
                        }
                    }

                    return index;
                }

                @Override
		public String toString() {
			StringBuilder buf = new StringBuilder();
			
			for (int i : this.field) {
				buf.append(Integer.toBinaryString(i));
				buf.append("_");
			}
			return buf.toString();
		}
	}

	private static final class Bitarray64 extends BitarrayFactory<Long> implements Serializable {
		private static final long serialVersionUID = 1185219475287404246L;
		private static final int OFFSET = 6;
		private static final int BITS = 1 << OFFSET;
		private static final int MASK = BITS - 1;
		private final long[] field;

		protected Bitarray64(int length) {
			super(length);
			int len = length >>> OFFSET;
			int o = length & MASK;

			if (o > 0)
				len++;

			this.field = new long[len];
		}

		/** is the bit set to 1? */
		@Override
		public boolean isOne(int index) {
			int i = index >>> OFFSET;
			int o = index & MASK;

			return (this.field[i] & (1L << o)) != 0 ? true : false;
		}

		@Override
		public void clear(int index) {
			int i = index >>> OFFSET;
			int o = index & MASK;

			this.field[i] &= ~(1L << o);
		}

		@Override
		public void set(int index) {
			int i = index >>> OFFSET;
			int o = index & MASK;

			this.field[i] |= (1L << o);
		}
		
		@Override
		public void flip(int index) {
			int i = index >>> OFFSET;
			int o = index & MASK;

			this.field[i] ^= (1L << o);
		}
		
		@Override
		public int nextOne(int index) {
			int i = index >>> OFFSET;
			long bit = 1L << (index & MASK);
			long word = this.field[i];

			do {
				index++;
				bit <<= 1;

				if (bit == 0L) {
					bit = 1L;
					if (++i == this.field.length) {
						return -1;
					}
					word = this.field[i];
				}
			} while ((word & bit) == 0L);

			return index;
		}
		
		/** number of bits that are set to 1 */
		@Override
		public int count() {
			int count = 0;

			for (int i=0; i<field.length; i++) {
				count += Long.bitCount(field[i]);
			}

			return count;
		}

		/** Set all bits to zero */
		@Override
		public void reset() {
			Arrays.fill(this.field, 0, this.field.length, 0L);
		}
		
		/** Set all bits to one */
		@Override
		public void preset() {
			Arrays.fill(this.field, 0, this.field.length, -1L);

			// set unused bits to zero, because of count()!
			int o = this.length & MASK;
			if (o > 0)
				field[field.length-1] &= (-1L >>> (BITS - o));
		}

		@Override
		public void setPattern(int[] pattern) {
			final long MASK32 = (1L << 32) - 1L;
			long word;
			int i = 0;
			int j = 0;
			
			while(j < this.field.length) {
				word = (long)pattern[i++] & MASK32;
				if (i == pattern.length) {
					i = 0;
				}
				word |= (long)pattern[i++] << 32;
				if (i == pattern.length) {
					i = 0;
				}
				this.field[j++] = word;
			}
		}

                @Override
                public int getIndicesArray(int[] indices, int after) {
                    int index = 0;
                    int fieldindex = after >>> OFFSET;
                    long bit = 1L << (after & MASK);
                    long word = this.field[fieldindex];

                    while (index < indices.length) {
                        after++;
                        bit <<= 1;

                        if (bit == 0L) {
                            bit = 1L;
                            if (++fieldindex == this.field.length) {
                                return index;
                            }
                            word = this.field[fieldindex];
                        }
                        
                        if ((word & bit) != 0L) {
                            indices[index++] = after;
                        }
                    }

                    return index;
                }

		@Override
		public String toString() {
			StringBuilder buf = new StringBuilder();

			for (long l : this.field) {
				buf.append(Long.toString(l,2));
				buf.append("_");
			}

			return buf.toString();
		}
	}
}