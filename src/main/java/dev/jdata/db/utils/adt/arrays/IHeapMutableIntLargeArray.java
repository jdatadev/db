package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntLargeArray extends IMutableIntLargeArray, IHeapArrayMarker {

    public static IHeapMutableIntLargeArray create() {

        return HeapMutableIntLargeArray.create(AllocationType.HEAP);
    }

    public static IHeapMutableIntLargeArray create(long initialCapacity) {

        return HeapMutableIntLargeArray.create(AllocationType.HEAP, initialCapacity);
    }

    public static IHeapMutableIntLargeArray create(int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        return HeapMutableIntLargeArray.create(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IHeapMutableIntLargeArray copyOf(IMutableIntLargeArray toCopy) {

        return HeapMutableIntLargeArray.copyOf(AllocationType.HEAP, toCopy);
    }
}
