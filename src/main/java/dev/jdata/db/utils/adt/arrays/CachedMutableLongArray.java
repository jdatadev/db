package dev.jdata.db.utils.adt.arrays;

final class CachedMutableLongArray extends MutableLongArray {

    CachedMutableLongArray(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);

        AllocationType.checkIsHeap(allocationType);
    }
}
