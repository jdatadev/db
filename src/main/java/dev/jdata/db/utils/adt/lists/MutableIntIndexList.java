package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.IntComparator;

abstract class MutableIntIndexList extends BaseIntIndexList implements IMutableIntIndexList {

    MutableIntIndexList(AllocationType allocationType) {
        super(allocationType, DEFAULT_INITIAL_CAPACITY);
    }

    MutableIntIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    MutableIntIndexList(AllocationType allocationType, IIntIterableElementsView mutableFrom) {
        super(allocationType, mutableFrom.toArray());

        mutableFrom.toArray();
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
    public final void sort(IntComparator comparator) {

        Objects.requireNonNull(comparator);

        Arrays.sort(getElementsArray(), 0, getIntNumElements());
    }

    @Override
    public final void addTail(int value) {

        final int[] dstArray = checkArrayCapacity(1);

        dstArray[getAndIncrementNumElements()] = value;
    }

    @Override
    public final void addTail(int ... values) {

        Checks.isNotEmpty(values);

        final int numValues = values.length;

        final int[] dstArray = checkArrayCapacity(numValues);
        final int numElements = getAndIncreaseNumElements(numValues);

        System.arraycopy(values, 0, dstArray, numElements, numValues);
    }

    @Override
    public final int setAndReturnPrevious(long index, int value) {

        Checks.checkLongIndex(index, getNumElements());

        final int intIndex = intIndex(index);

        final int[] elementsArray = getElementsArray();

        final int result = elementsArray[intIndex];

        elementsArray[intIndex] = value;

        return result;
    }

    @Override
    public final int removeTailAndReturnValue() {

        if (isEmpty()) {

            throw new IllegalStateException();
        }

        final int result = getTail();

        decrementNumElements();

        return result;
    }

    private boolean removeAtMostOne(int value) {

        final boolean result;

        final int num = getIntNumElements();
        final int[] elementsArray = getElementsArray();

        if (num == 1) {

            if (elementsArray[0] == value) {

                clear();

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
        final int[] elementsArray = getElementsArray();

        int foundIndex = -1;

        for (int i = 0; i < num; ++ i) {

            if (elementsArray[i] == value) {

                if (foundIndex != -1) {

                    throw new IllegalStateException();
                }

                foundIndex = i;
            }
        }

        return foundIndex;
    }

    private int[] checkArrayCapacity(int numElementsToAdd) {

        return checkArrayCapacity(numElementsToAdd, null, a -> a.length, (p, c) -> new int[c]);
    }
}
