package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.LongComparator;

abstract class MutableLongIndexList extends BaseLongIndexList implements IMutableLongIndexList {

    MutableLongIndexList(AllocationType allocationType) {
        super(allocationType, DEFAULT_INITIAL_CAPACITY);
    }

    MutableLongIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    MutableLongIndexList(AllocationType allocationType, ILongIterableElementsView mutableFrom) {
        super(allocationType, mutableFrom.toArray());
    }

    @Override
    public final long getCapacity() {

        return getElementsCapacity();
    }

    @Override
    public final void clear() {

        clearElements();
    }

    @Override
    public final void sort(LongComparator comparator) {

        Objects.requireNonNull(comparator);

        Arrays.sort(getElementsArray(), 0, getIntNumElements());
    }

    @Override
    public final void addTail(long value) {

        final long[] dstArray = checkArrayCapacity(1);

        dstArray[getAndIncrementNumElements()] = value;
    }

    @Override
    public final void addTail(long ... values) {

        Checks.isNotEmpty(values);

        final int numValues = values.length;

        final long[] dstArray = checkArrayCapacity(numValues);

        final int numElements = getAndIncreaseNumElements(numValues);

        System.arraycopy(dstArray, 0, dstArray, numElements, numValues);
    }

    @Override
    public final long setAndReturnPrevious(long index, long value) {

        Checks.checkLongIndex(index, getNumElements());

        final int intIndex = intIndex(index);

        final long[] elementsArray = getElementsArray();

        final long result = elementsArray[intIndex];

        elementsArray[intIndex] = value;

        return result;
    }

    @Override
    public final long removeTailAndReturnValue() {

        if (isEmpty()) {

            throw new IllegalStateException();
        }

        final long result = getTail();

        decrementNumElements();

        return result;
    }

    private boolean removeAtMostOne(long value) {

        final boolean result;

        final int num = getIntNumElements();
        final long[] elementsArray = getElementsArray();

        if (num == 1) {

            if (elementsArray[0] == value) {

                clearElements();

                result = true;
            }
            else {
                result = false;
            }
        }
        else {
            final int index = findAtMostOne(value);

            if (index != -1) {

                if (index == 0) {

                    Array.move(elementsArray, 1, num - 1, -1);
                }
                else if (index == num - 1) {

                }
                else {
                    Array.move(elementsArray, index + 1, num - index - 1, -1);
                }

                decrementNumElements();

                result = true;
            }
            else {
                result = false;
            }
        }

        return result;
    }

    private int findAtMostOne(long value) {

        final int num = getIntNumElements();
        final long[] elementsArray = getElementsArray();

        int foundIndex = -1;

        for (int i = 0; i < num; ++ i) {

            if (elementsArray[i] == value) {

                if (foundIndex != -1) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                foundIndex = i;
            }
        }

        return foundIndex;
    }

    private long[] checkArrayCapacity(int numElementsToAdd) {

        return checkArrayCapacity(numElementsToAdd, null, a -> a.length, (p, c) -> new long[c]);
    }
}
