package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableLongArray extends IMutableLongArray, IHeapArrayMarker {

    public static IHeapMutableLongArray create() {

        return Capacity.instantiateArray(AllocationType.HEAP, HeapMutableLongArray::new);
    }

    public static IHeapMutableLongArray create(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableLongArray(AllocationType.HEAP, initialCapacity);
    }
}
