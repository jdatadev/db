package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableCharLargeArray extends IMutableCharLargeArray, IHeapArrayMarker {

    public static IHeapMutableCharLargeArray create() {

        return HeapMutableCharLargeArray.create(AllocationType.HEAP);
    }

    public static IHeapMutableCharLargeArray create(long initialCapacity) {

        return HeapMutableCharLargeArray.create(AllocationType.HEAP, initialCapacity);
    }

    public static IHeapMutableCharLargeArray create(int initialOuterCapacity, int innerCapacityExponent, char clearValue) {

        return HeapMutableCharLargeArray.create(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    public static IHeapMutableCharLargeArray copyOf(IMutableCharLargeArray toCopy) {

        return HeapMutableCharLargeArray.copyOf(AllocationType.HEAP, toCopy);
    }
}
