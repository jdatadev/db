package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableStringsCharLargeArray extends IMutableStringsCharLargeArray, IHeapArrayMarker {

    public static IHeapMutableStringsCharLargeArray create(int initialOuterCapacity, int innerCapacityExponent) {

        return HeapMutableStringsCharLargeArray.create(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent);
    }
}
