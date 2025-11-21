package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.arrays.LargeArray;
import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.LongComparator;
import dev.jdata.db.utils.scalars.Integers;

@Deprecated // currently not in use
public final class MutableLongLargeArrayList extends LargeArray<long[][], long[]> implements IMutableLongLargeIndexList {

    private long numElements;

    public MutableLongLargeArrayList(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, false, long[][]::new);
    }

    @Override
    public long getCapacity() {

        return getArrayElementCapacity();
    }

    @Override
    public long getNumElements() {

        return numElements;
    }

    @Override
    public void clear() {

        clearArray();

        this.numElements = 0L;
    }

    @Override
    public <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEachWithResult(null, parameter, forEach, (e, p, f) -> {

            f.each(e, p);

            return null;
        });
    }

    @Override
    public <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final long[][] array = getOuterArray();

        final int numOuterUtilizedEntries = getNumOuterUtilizedEntries();

        switch (numOuterUtilizedEntries) {

        case 0:
            break;

        case 1: {

            final long num = numElements;

            final long[] inner = array[0];

            for (int i = 0; i < num; ++ i) {

                final R forEachResult = forEach.each(inner[i], parameter1, parameter2);

                if (forEachResult != null) {

                    result = forEachResult;
                    break;
                }
            }
            break;
        }

        default: {

            final long innerElementCapacity = getInnerElementCapacity();

            final int numCompleteOuter = numOuterUtilizedEntries - 1;

            outer:

                for (int i = 0; i < numCompleteOuter; ++ i) {

                    final long[] inner = array[i];

                    for (int j = 0; j < innerElementCapacity; ++ j) {

                        final R forEachResult = forEach.each(inner[j], parameter1, parameter2);

                        if (forEachResult != null) {

                            result = forEachResult;
                            break outer;
                        }
                    }
                }

            final long remaining = Capacity.getRemainderOfLastInnerArrayWithLimit(innerElementCapacity, numElements, getNumOuterUtilizedEntries(), numCompleteOuter - 1);
            final long num = remaining;

            final long[] inner = array[numCompleteOuter];

            for (int i = 0; i < num; ++ i) {

                final R forEachResult = forEach.each(inner[i], parameter1, parameter2);

                if (forEachResult != null) {

                    result = forEachResult;
                    break;
                }
            }
            break;
        }
        }

        return result;
    }

    @Override
    public boolean contains(long value) {

        boolean found = false;

        if (!isEmpty()) {

            final long[][] arrays = getOuterArray();

            final int numOuter = getNumOuterUtilizedEntries();
            final int numOuterMinusOne = numOuter - 1;
            final long innerElementCapacity = getInnerElementCapacity();

            int i;

            outer:

                for (i = 0; i < numOuterMinusOne; ++ i) {

                    final long[] array = arrays[i];

                    for (int j = 0; j < innerElementCapacity; ++ j) {

                        if (array[j] == value) {

                            found = true;
                            break outer;
                        }
                    }
                }

            if (!found) {

                final long remainder = numElements & getInnerIndexMask();

                final long[] array = arrays[i];

                for (int j = 0; j < remainder; ++ j) {

                    if (array[j] == value) {

                        found = true;
                        break;
                    }
                }
            }
        }

        return found;
    }

    @Override
    public <P> boolean contains(P parameter, ILongElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsOnly(long value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsOnly(long value, ILongContainsOnlyPredicate predicate) {

        Objects.requireNonNull(predicate);

        throw new UnsupportedOperationException();
    }

    @Override
    public <P> long findAtMostOneIndex(P parameter, ILongElementPredicate<P> predicate) {

        throw new UnsupportedOperationException();
    }

    @Override
    public long[] toArray() {

        throw new UnsupportedOperationException();
    }

    @Override
    public long getIndexLimit() {

        throw new UnsupportedOperationException();
    }

    @Override
    public long get(long index) {

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    @Override
    public long removeTailAndReturnValue() {

        throw new UnsupportedOperationException();
    }

    @Override
    public void addTail(long value) {

        final long limit = numElements;

        final int outerIndex = checkCapacityForOneAppendedElementAndReturnOuterIndex(limit);

        final long index = limit;

        ++ numElements;

        getOuterArray()[outerIndex][getInnerElementIndex(index)] = value;
    }

    @Override
    public void set(long index, long value) {

        Checks.checkLongIndex(index, numElements);

        getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)] = value;
    }

    @Override
    public long setAndReturnPrevious(long index, long value) {

        Checks.checkLongIndex(index, numElements);

        final int outerIndex = getOuterIndex(index);
        final int innerIndex = getInnerElementIndex(index);

        final long[][] outerArray = getOuterArray();

        final long result = outerArray[outerIndex][innerIndex];

        outerArray[outerIndex][innerIndex] = value;

        return result;
    }

    @Override
    public final void sort(LongComparator comparator) {

        Objects.requireNonNull(comparator);

        throw new UnsupportedOperationException();
    }

    @Override
    protected int getOuterArrayLength(long[][] outerArray) {

        Objects.requireNonNull(outerArray);

        return outerArray.length;
    }

    @Override
    protected long[][] copyOuterArray(long[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayLength(outerArray, newCapacity);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected long[] getInnerArray(long[][] outerArray, int index) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, index);

        return outerArray[index];
    }

    @Override
    protected void clearInnerArray(long[] innerArray, long startIndex, long numElements) {

        throw new UnsupportedOperationException();
    }

    @Override
    protected long[] abstractCreateAndSetInnerArray(long[][] outerArray, int outerIndex, long innerArrayElementCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.checkArrayIndex(outerArray, outerIndex);
        Checks.isIntInnerElementCapacity(innerArrayElementCapacity);

        final int innerArrayLength = Integers.checkUnsignedLongToUnsignedInt(innerArrayElementCapacity);

        return outerArray[outerIndex] = new long[innerArrayLength];
    }
}
