package dev.jdata.db.utils.adt.arrays;

abstract class LongArray extends BaseLongArray implements ILongArray {

    LongArray(AllocationType allocationType, long[] elements) {
        super(allocationType, elements, elements.length, false);
    }

    LongArray(AllocationType allocationType, BaseLongArray toCopy) {
        super(allocationType, toCopy);
    }

    @Override
    final long[] reallocate(long[] elementsArray, int newCapacity) {

        checkReallocateParameters(elementsArray, elementsArray.length, newCapacity);

        throw new UnsupportedOperationException();
    }

    @Override
    final void clearElementsArray(long[] elementsArray, int startIndex, int numElements) {

        checkClearElementsArrayParameters(elementsArray, elementsArray.length, startIndex, numElements);

        throw new UnsupportedOperationException();
    }
}
