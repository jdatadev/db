package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseLargeByteArray extends LargeExponentArray<byte[][], byte[]> implements IClearable {

    protected BaseLargeByteArray(int innerCapacityExponent, boolean requiresInnerArrayNumElements) {
        this(0, innerCapacityExponent, requiresInnerArrayNumElements);
    }

    BaseLargeByteArray(int initialOuterCapacity, int innerCapacityExponent, boolean requiresInnerArrayNumElements) {
        super(initialOuterCapacity, byte[][]::new, a -> a.length, innerCapacityExponent, initialOuterCapacity, requiresInnerArrayNumElements);

        Checks.isCapacity(initialOuterCapacity);

        if (initialOuterCapacity > 0) {

            setInnerArray(0, new byte[getInnerNumAllocateElements()]);
            setNumInnerElements(0, 0);
        }
    }

    final byte[] getByteArrayByOffset(long byteOffset) {

        Checks.isOffset(byteOffset);

        return getOuterArray()[getOuterIndex(byteOffset)];
    }

    protected final byte[] getByteArrayByOuterIndex(int outerIndex) {

        Checks.isIndex(outerIndex);

        return getOuterArray()[outerIndex];
    }

    protected final int getLastByteArrayIndexOrAllocateForOneAppendedElement() {

        final int numOuter = getNumOuterUtilizedEntries();

        final int result;

        if (numOuter != 0) {

            result = numOuter - 1;
        }
        else {
            result = checkCapacityForOneAppendedElementAndReturnOuterIndex();
        }

        return result;
    }

    @Override
    protected final byte[] abstractCreateAndSetInnerArray(byte[][] outerArray, int index, int innerArrayLength) {

        return outerArray[index] = new byte[innerArrayLength];
    }

    @Override
    protected final byte[] getInnerArray(byte[][] outerArray, int index) {

        return outerArray[index];
    }

    @Override
    protected final byte[][] copyOuterArray(byte[][] outerArray, int newCapacity) {

        return Arrays.copyOf(outerArray, newCapacity);
    }

    private void setInnerArray(int outerIndex, byte[] innerArray) {

        Checks.isIndex(outerIndex);
        Objects.requireNonNull(innerArray);

        getOuterArray()[outerIndex] = innerArray;
    }
}
