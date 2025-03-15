package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

public final class LargeByteArray extends BaseLargeByteArray {

    public LargeByteArray(int innerCapacityExponent) {
        super(innerCapacityExponent);
    }

    public LargeByteArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    protected void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    public int get(long index) {

        return getBuffer(index)[getInnerIndexNotCountingNumElements(index) + NUM_INNER_ELEMENTS_NUM_BYTES];
    }

    public void add(byte value) {

        final byte[] buffer = checkCapacity();

        buffer[getInnerIndexNotCountingNumElements(getNumElements()) + NUM_INNER_ELEMENTS_NUM_BYTES] = value;

        incrementNumElements();

        incrementNumElements(buffer);
    }

    public void set(long index, byte value) {

        Objects.checkIndex(index, getNumElements());

        final byte[] array = getBuffer(index);

        final int innerIndex = getInnerIndexNotCountingNumElements(index);

        array[innerIndex + NUM_INNER_ELEMENTS_NUM_BYTES] = value;
    }
}
