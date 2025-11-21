package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableStringsCharLargeArray extends IMutableStringsCharLargeArray, IHeapArrayMarker {

    public static IHeapMutableStringsCharLargeArray create(int initialOuterCapacity, int innerCapacityExponent) {

        Checks.isInitialOuterCapacity(initialOuterCapacity);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);

        return new HeapMutableStringsCharLargeArray(AllocationType.HEAP, initialOuterCapacity, innerCapacityExponent);
    }
}
