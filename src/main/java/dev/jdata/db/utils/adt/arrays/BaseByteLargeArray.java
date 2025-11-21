package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseByteLargeArray extends ExponentLargeArray<byte[][], byte[]> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABlE_BASE_BYTE_LARGE_ARRAY;

    private final byte clearValue;

    protected abstract int getInnerByteArrayLength(long innerArrayElementCapacity);

    protected BaseByteLargeArray(AllocationType allocationType, int innerCapacityExponent, boolean requiresInnerArrayNumElements) {
        this(allocationType, DEFAULT_INITIAL_OUTER_CAPACITY, innerCapacityExponent, (byte)0, false, requiresInnerArrayNumElements);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("innerCapacityExponent", innerCapacityExponent)
                    .add("requiresInnerArrayNumElements", requiresInnerArrayNumElements));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseByteLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, byte clearValue, boolean hasClearValue,
            boolean requiresInnerArrayNumElements) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, initialOuterCapacity, hasClearValue, byte[][]::new, requiresInnerArrayNumElements);

        Checks.isInitialOuterCapacity(initialOuterCapacity);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("clearValue", clearValue).add("hasClearValue", hasClearValue).add("requiresInnerArrayNumElements", requiresInnerArrayNumElements));
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

    BaseByteLargeArray(AllocationType allocationType, BaseByteLargeArray toCopy) {
        super(allocationType, toCopy, (s, d, n) -> {

            final int length = s.length;

            Checks.areEqual(length, d.length);

            for (int i = 0; i < n; ++ i) {

                final byte[] a = s[i];

                d[i] = a != null ? Array.copyOf(a) : null;
            }
        });

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        this.clearValue = toCopy.clearValue;
    }

    final byte[] getByteArrayByOffset(long byteOffset) {

        Checks.isLongOffset(byteOffset);

        return getOuterArray()[getOuterIndex(byteOffset)];
    }

    protected final byte[] getByteArrayByOuterIndex(int outerIndex) {

        Checks.isOuterIndex(outerIndex);

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

        Arrays.fill(innerArray, IByIndexView.intIndex(startIndex), IByIndexView.intIndex(startIndex + numElements), clearValue);
    }

    @Override
    protected final byte[] abstractCreateAndSetInnerArray(byte[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isLongLengthAboveZero(innerArrayElementCapacity);

        final int innerArrayLength = getInnerByteArrayLength(innerArrayElementCapacity);

        return outerArray[outerIndex] = new byte[innerArrayLength];
    }

    private void setInnerArray(int outerIndex, byte[] innerArray) {

        Checks.isOuterIndex(outerIndex);
        Objects.requireNonNull(innerArray);

        getOuterArray()[outerIndex] = innerArray;
    }
}
