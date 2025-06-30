package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

public final class LargeByteArray extends BaseLargeByteArray implements IArray {

    private static final boolean REQUIRES_INNER_ARRAY_NUM_ELEMENTS = false;

    private long limit;

    public LargeByteArray(int innerCapacityExponent) {
        super(innerCapacityExponent, REQUIRES_INNER_ARRAY_NUM_ELEMENTS);

        this.limit = 0L;
    }

    public LargeByteArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent, REQUIRES_INNER_ARRAY_NUM_ELEMENTS);

        this.limit = 0L;
    }

    @Override
    public void clear() {

        super.clear();

        this.limit = 0L;
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    public long getLimit() {

        return limit;
    }

    public int get(long index) {

        return getByteArrayByOffset(index)[getInnerElementIndex(index)];
    }

    public void add(byte value) {

        final int outerIndex = checkCapacityForOneAppendedElementAndReturnOuterIndex();

        final byte[] byteArray = getOuterArray()[outerIndex];

        byteArray[getInnerElementIndex(getNumElements())] = value;

        incrementNumElements();
    }

    public void set(long index, byte value) {

        if (index >= limit) {

            ensureCapacityAndLimit(index, limit, this, null, (t, i) -> t.limit += i, null);
        }
        else {
            Objects.checkIndex(index, getNumElements());
        }

        final byte[] array = getByteArrayByOffset(index);

        array[getInnerElementIndex(index)] = value;
    }

    @Override
    final long getRemainderOfLastInnerArray(int outerIndex) {

        return getRemainderOfLastInnerArrayWithLimit(outerIndex, limit);
    }

    private long getNumElements() {

        return limit;
    }

    private void incrementNumElements() {

        ++ limit;
    }
}
