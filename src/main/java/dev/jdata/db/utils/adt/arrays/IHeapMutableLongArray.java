package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongArray extends IMutableLongArray, IHeapArrayMarker {

    public static IHeapMutableLongArray create() {

        return HeapMutableLongArray.create(AllocationType.HEAP);
    }

    public static IHeapMutableLongArray create(int initialCapacity) {

        return HeapMutableLongArray.create(AllocationType.HEAP, initialCapacity);
    }

    public static IHeapMutableLongArray create(int initialCapacity, long clearValue) {

        return HeapMutableLongArray.create(AllocationType.HEAP, initialCapacity, clearValue);
    }
}
