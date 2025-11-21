package dev.jdata.db.utils.adt.numbers.integers;

final class HeapMutableLargeInteger extends MutableLargeInteger<HeapMutableLargeInteger> implements IHeapMutableLargeInteger {

    static HeapMutableLargeInteger create(AllocationType allocationType, int precision) {

        AllocationType.checkIsHeap(allocationType);

        return new HeapMutableLargeInteger(allocationType, precision);
    }

    static HeapMutableLargeInteger create(AllocationType allocationType, ILargeIntegerView largeInteger) {

        AllocationType.checkIsHeap(allocationType);

        return new HeapMutableLargeInteger(allocationType, largeInteger);
    }

    private HeapMutableLargeInteger(AllocationType allocationType, int precision) {
        super(allocationType, precision);
    }

    private HeapMutableLargeInteger(AllocationType allocationType, ILargeIntegerView largeInteger) {
        super(allocationType, largeInteger);
    }
}
