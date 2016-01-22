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
public interface Bitfield {
    /** @return the size of this bitfield */
    long size();

    /** @return count the set bits of this field */
    long count();
    
    /** set a bit to one */
    void set(long index);
    
    /** set a bit to zero */
    void clear(long index);
    
    /** flip a bit 0 -> 1 or 1 -> 0 */
    void flip(long index);
    
    /** test the value of a bit */
    boolean isOne(long index);
}
