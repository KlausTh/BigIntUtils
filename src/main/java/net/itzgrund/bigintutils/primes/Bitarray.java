package net.itzgrund.bigintutils.primes;

public interface Bitarray {

    /**
     * number of bits in field
     */
    int size();

    /**
     * is the bit set to 1?
     */
    boolean isOne(int index);

    /**
     * reset a bit to zero
     */
    void clear(int index);

    /**
     * set a bit to one
     */
    void set(int index);

    /**
     * flip a bit 0 -> 1 or 1 -> 0
     */
    void flip(int index);

    /**
     * Search the next set bit after a number
     *
     * @return index of the next one (-1 not found)
     */
    int nextOne(int after);

    /**
     * number of bits that are set to 1
     */
    int count();

    /**
     * Set all bits to zero
     */
    void reset();

    /**
     * Set all bits to one
     */
    void preset();

    /**
     * Loop over pattern and set all bits in field
     */
    void setPattern(int[] pattern);

    /**
     * Fill the Array with Index of set Bits
     *
     * @param array buffer for saving indices
     * @param after copy after this index
     * @return number of indices copied
     */
    int getIndicesArray(int[] array, int after);
}
