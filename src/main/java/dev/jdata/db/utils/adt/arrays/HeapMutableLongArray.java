package dev.jdata.db.utils.adt.arrays;

final class HeapMutableLongArray extends MutableLongArray implements IHeapMutableLongArray {

    HeapMutableLongArray(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    HeapMutableLongArray(AllocationType allocationType, int initialCapacity, long clearValue) {
        super(allocationType, initialCapacity, clearValue);
    }

    HeapMutableLongArray(AllocationType allocationType, MutableLongArray toCopy) {
        super(allocationType, toCopy);
    }
}
