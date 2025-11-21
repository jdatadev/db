package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableIntArray extends IMutableIntArray, IHeapArrayMarker {

    public static IHeapMutableIntArray create(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableIntArray(AllocationType.HEAP, initialCapacity);
    }

    public static IHeapMutableIntArray create(int initialCapacity, int clearValue) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableIntArray(AllocationType.HEAP, initialCapacity, clearValue);
    }
}
