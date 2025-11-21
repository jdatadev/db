package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntArray extends IMutableIntArray, IHeapArrayMarker {

    public static IHeapMutableIntArray create(int initialCapacity) {

        return HeapMutableIntArray.create(AllocationType.HEAP, initialCapacity);
    }

    public static IHeapMutableIntArray create(int initialCapacity, int clearValue) {

        return HeapMutableIntArray.create(AllocationType.HEAP, initialCapacity, clearValue);
    }
}
