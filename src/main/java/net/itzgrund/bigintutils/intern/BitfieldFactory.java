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
package net.itzgrund.bigintutils.intern;

/**
 *
 * @author klaus
 */
public abstract class BitfieldFactory {
    public static Bitfield create(long size) {
        return new IntegerBitfield(size);
    }
    
    private final static class IntegerBitfield implements Bitfield {
        private final long size;
        private final int[] field;

        public IntegerBitfield(long size) {
            this.size = size;
            int length = getIndex(size);
            length += Math.min(1, getOffset(size));
            
            this.field = new int[length];
        }
        
        private int getIndex(long index) {
            return (int)(index / Integer.SIZE);
        }
        
        private int getOffset(long index) {
            return (int)index % Integer.SIZE;
        }

        @Override
        public long size() {
            return this.size;
        }

        @Override
        public long count() {
            long count = 0;
            
            for (int i : this.field) {
                count += Integer.bitCount(i);
            }
            
            return count;
        }

        @Override
        public void set(long index) {
            int i = getIndex(index);
            int o = getOffset(index);
            
            this.field[i] |= (1 << o);
        }

        @Override
        public void clear(long index) {
            int i = getIndex(index);
            int o = getOffset(index);
            
            this.field[i] &= ~(1 << o);
        }

        @Override
        public void flip(long index) {
            int i = getIndex(index);
            int o = getOffset(index);

            this.field[i] ^= (1 << o);
        }

        @Override
        public boolean isOne(long index) {
            int i = getIndex(index);
            int o = getOffset(index);
            
            return (this.field[i] & (1 << o)) != 0;
        }
        
    }
}
