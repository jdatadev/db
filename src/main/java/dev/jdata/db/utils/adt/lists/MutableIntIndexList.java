package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.checks.Checks;

abstract class MutableIntIndexList extends BaseIntIndexList implements IMutableIntIndexList {

    MutableIntIndexList(AllocationType allocationType) {
        super(allocationType, DEFAULT_INITIAL_CAPACITY);
    }

    MutableIntIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
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
    public final void addTail(int value) {

        final int [] elementsArray = getElementsArray();
        final int arrayLength = elementsArray.length;
        final int numElements = getIntNumElements();

        final int[] dstArray;

        if (numElements == arrayLength) {

            dstArray = Arrays.copyOf(elementsArray, increaseCapacity(arrayLength));

            setArray(dstArray);
        }
        else {
            dstArray = elementsArray;
        }

        dstArray[numElements] = value;

        incrementNumElements();
    }

    @Override
    public final void addTail(int ... values) {

        Checks.isNotEmpty(values);

        final int num = getIntNumElements();
        final int numValues = values.length;

        final int numTotal = num + numValues;

        final int[] elementsArray = getElementsArray();

        final int[] dstArray;

        if (numTotal > elementsArray.length) {

            dstArray = Arrays.copyOf(elementsArray, increaseCapacity(numTotal));

            setArray(dstArray);
        }
        else {
            dstArray = elementsArray;
        }

        System.arraycopy(values, 0, dstArray, num, numValues);

        setNumElements(numTotal);
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
}
