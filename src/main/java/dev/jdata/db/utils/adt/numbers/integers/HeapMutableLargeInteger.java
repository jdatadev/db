package dev.jdata.db.utils.adt.numbers.integers;

final class HeapMutableLargeInteger extends MutableLargeInteger<HeapMutableLargeInteger> implements IHeapMutableLargeInteger {

    HeapMutableLargeInteger(AllocationType allocationType, int precision) {
        super(allocationType, precision);

        AllocationType.checkIsHeap(allocationType);
    }
}
