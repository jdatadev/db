package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.arrays.LargeArray;

@Deprecated // currently not in use
public final class LargeLongArrayList extends LargeArray<long[][], long[]> implements IMutableLongIndexList {

    private long numElements;

    public LargeLongArrayList(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, long[][]::new, a -> a.length, innerCapacityExponent);
    }

    @Override
    public long getNumElements() {

        return numElements;
    }

    @Override
    public void clear() {

        super.clear();

        this.numElements = 0L;
    }

    @Override
    public <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E {

        forEachWithResult(null, parameter, forEach, (e, p, f) -> {

            f.each(e, p);

            return null;
        });
    }

    @Override
    public <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, E> forEach) throws E {

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

            final long remaining = Capacity.getRemainderOfLastInnerArrayWithLimit(numCompleteOuter - 1, numElements, getNumOuterUtilizedEntries(), innerElementCapacity);
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
    public long get(long index) {

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    @Override
    public boolean contains(long value) {

        boolean found = false;

        if (!isEmpty()) {

            final long[][] arrays = getOuterArray();

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
    public void addTail(long value) {

        checkCapacity();

        final long index = this.numElements ++;

        getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)] = value;
    }

    public void set(long index, long value) {

        Objects.checkIndex(index, numElements);

        getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)] = value;
    }

    @Override
    public boolean removeAtMostOne(long value) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    protected long[][] copyOuterArray(long[][] outerArray, int capacity) {

        return Arrays.copyOf(outerArray, capacity);
    }

    @Override
    protected int getOuterArrayLength(long[][] outerArray) {

        return outerArray.length;
    }

    @Override
    protected long[] getInnerArray(long[][] outerArray, int index) {

        return outerArray[index];
    }

    @Override
    protected long[] abstractCreateAndSetInnerArray(long[][] outerArray, int outerIndex, int innerArrayLength) {

        return outerArray[outerIndex] = new long[innerArrayLength];
    }
}
