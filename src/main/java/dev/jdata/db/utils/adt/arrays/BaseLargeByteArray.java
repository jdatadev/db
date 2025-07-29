package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseLargeByteArray extends LargeExponentArray<byte[][], byte[]> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_BYTE_ARRAY;

    private final byte clearValue;

    protected abstract int getInnerByteArrayLength(long innerArrayElementCapacity);

    protected BaseLargeByteArray(int innerCapacityExponent, boolean requiresInnerArrayNumElements) {
        this(DEFAULT_INITIAL_OUTER_CAPACITY, innerCapacityExponent, (byte)0, false, requiresInnerArrayNumElements);

        if (DEBUG) {

            enter(b -> b.add("innerCapacityExponent", innerCapacityExponent).add("requiresInnerArrayNumElements", requiresInnerArrayNumElements));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLargeByteArray(int initialOuterCapacity, int innerCapacityExponent, byte clearValue, boolean hasClearValue, boolean requiresInnerArrayNumElements) {
        super(initialOuterCapacity, innerCapacityExponent, initialOuterCapacity, hasClearValue, byte[][]::new, requiresInnerArrayNumElements);

        Checks.isCapacity(initialOuterCapacity);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("clearValue", clearValue)
                    .add("hasClearValue", hasClearValue).add("requiresInnerArrayNumElements", requiresInnerArrayNumElements));
        }

        this.clearValue = clearValue;

        if (initialOuterCapacity > 0) {

            final int innerArrayLength = getInnerByteArrayLength(getInnerNumAllocateElements());

            setInnerArray(0, new byte[innerArrayLength]);

            if (requiresInnerArrayNumElements) {

                setNumInnerElements(0, 0);
            }
        }

        if (DEBUG) {

            exit();
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

    protected final int getLastByteArrayIndexOrAllocateForOneAppendedElement(long limit) {

        Checks.isArrayLimit(limit);

        final int numOuter = getNumOuterUtilizedEntries();

        final int result;

        if (numOuter != 0) {

            result = numOuter - 1;
        }
        else {
            result = checkCapacityForOneAppendedElementAndReturnOuterIndex(limit);
        }

        return result;
    }

    @Override
    protected final int getOuterArrayLength(byte[][] outerArray) {

        Objects.requireNonNull(outerArray);

        return outerArray.length;
    }

    @Override
    protected final byte[][] copyOuterArray(byte[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.isGreaterThanOrEqualTo(newCapacity, outerArray.length);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected final byte[] getInnerArray(byte[][] outerArray, int index) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, index);

        return outerArray[index];
    }

    @Override
    protected void clearInnerArray(byte[] innerArray, long startIndex, long numElements) {

        Objects.requireNonNull(innerArray);
        Checks.checkFromIndexSize(startIndex, numElements, innerArray.length);

        assertShouldClear();

        Arrays.fill(innerArray, Integers.checkUnsignedLongToUnsignedInt(startIndex), Integers.checkUnsignedLongToUnsignedInt(startIndex + numElements), clearValue);
    }

    @Override
    protected final byte[] abstractCreateAndSetInnerArray(byte[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isCapacity(innerArrayElementCapacity);

        final int innerArrayLength = getInnerByteArrayLength(innerArrayElementCapacity);

        return outerArray[outerIndex] = new byte[innerArrayLength];
    }

    private void setInnerArray(int outerIndex, byte[] innerArray) {

        Checks.isIndex(outerIndex);
        Objects.requireNonNull(innerArray);

        getOuterArray()[outerIndex] = innerArray;
    }
}
