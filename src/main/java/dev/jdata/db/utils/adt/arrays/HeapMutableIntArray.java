package dev.jdata.db.utils.adt.arrays;

final class HeapMutableIntArray extends MutableIntArray implements IHeapMutableIntArray {

    HeapMutableIntArray(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    HeapMutableIntArray(AllocationType allocationType, int initialCapacity, int clearValue) {
        super(allocationType, initialCapacity, clearValue);
    }
}
