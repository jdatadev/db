package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.ILongByIndex;
import dev.jdata.db.utils.adt.elements.ILongElements;
import dev.jdata.db.utils.adt.elements.ILongElementsMutators;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.scalars.Integers;

@Deprecated // currently not in use
public final class LargeLongArrayList extends LargeArray<long[][], long[]> implements IMutableElements, ILongElementsMutators, ILongElements, ILongByIndex {

    private long numElements;

    public LargeLongArrayList(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent, 0, long[][]::new);
    }

    @Override
    public boolean isEmpty() {

        return numElements == 0;
    }

    @Override
    public long getNumElements() {

        return numElements;
    }

    @Override
    public void clear() {

        reset();

        this.numElements = 0L;
    }

    @Override
    public <P, E extends Exception> void forEach(P parameter, ForEach<P, E> forEach) throws E {

        final long[][] array = getArray();

        final int numOuterUtilizedEntries = getNumOuterUtilizedEntries();

        switch (numOuterUtilizedEntries) {

        case 0:
            break;

        case 1: {

            final int innerArrayLengthNumElements = getInnerArrayLengthNumElements();
            final long num = numElements + innerArrayLengthNumElements;

            final long[] inner = array[0];

            for (int i = innerArrayLengthNumElements; i < num; ++ i) {

                forEach.each(inner[i], parameter);
            }
            break;
        }

        default: {

            final int innerArrayLengthNumElements = getInnerArrayLengthNumElements();
            final int innerCapacity = getInnerCapacity();

            final long numCompleteInner = innerCapacity + innerArrayLengthNumElements;

            final int numCompleteOuter = numOuterUtilizedEntries - 1;

            for (int i = 0; i < numCompleteOuter; ++ i) {

                final long[] inner = array[i];

                for (int j = innerArrayLengthNumElements; j < numCompleteInner; ++ j) {

                    forEach.each(inner[j], parameter);
                }
            }

            final int remaining = Integers.checkUnsignedLongToUnsignedInt(numElements % innerCapacity);
            final long num = remaining + innerArrayLengthNumElements;

            final long[] inner = array[numCompleteOuter];

            for (int i = innerArrayLengthNumElements; i < num; ++ i) {

                forEach.each(inner[i], parameter);
            }
            break;
        }
        }
    }

    @Override
    public long get(long index) {

        return getArray()[getOuterIndex(index)][getInnerIndex(index)];
    }

    @Override
    public boolean contains(long value) {

        boolean found = false;

        if (!isEmpty()) {

            final long[][] arrays = getArray();

            final int numOuter = getNumOuterUtilizedEntries();
            final int numOuterMinusOne = numOuter - 1;
            final int innerCapacity = getInnerCapacity();

            int i;

            outer:

                for (i = 0; i < numOuterMinusOne; ++ i) {

                    final long[] array = arrays[i];

                    for (int j = 0; j < innerCapacity; ++ j) {

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
    public void add(long value) {

        checkCapacity();

        final long index = this.numElements ++;

        getArray()[getOuterIndex(index)][getInnerIndex(index)] = value;
    }

    public void set(long index, long value) {

        Objects.checkIndex(index, numElements);

        getArray()[getOuterIndex(index)][getInnerIndex(index)] = value;
    }

    @Override
    public boolean remove(long value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    long[][] copyOuterArray(long[][] outerArray, int capacity) {

        return Arrays.copyOf(outerArray, capacity);
    }

    @Override
    int getOuterArrayLength(long[][] outerArray) {

        return outerArray.length;
    }

    @Override
    long[] getInnerArray(long[][] outerArray, int index) {

        return outerArray[index];
    }

    @Override
    int getInnerArrayLength(long[] innerArray) {

        return innerArray.length;
    }

    @Override
    void setInnerArrayLength(long[] innerArray, int length) {

    }

    @Override
    int getNumInnerElements(long[] innerArray) {

        return innerArray.length;
    }

    @Override
    long[] setInnerArray(long[][] outerArray, int outerIndex, int innerArrayLength) {

        return outerArray[outerIndex] = new long[innerArrayLength];
    }

    private void checkCapacity() {

        checkCapacity(null, null);
    }
}
