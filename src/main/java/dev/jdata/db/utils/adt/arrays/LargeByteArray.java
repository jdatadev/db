package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

public final class LargeByteArray extends BaseLargeByteArray implements IArray {

    private long limit;

    public LargeByteArray(int innerCapacityExponent) {
        super(innerCapacityExponent);

        this.limit = 0L;
    }

    public LargeByteArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent);

        this.limit = 0L;
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    public boolean isEmpty() {

        return limit == 0L;
    }

    @Override
    public long getLimit() {

        return limit;
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

    private long getNumElements() {

        return limit;
    }

    private void incrementNumElements() {

        ++ limit;
    }
}
