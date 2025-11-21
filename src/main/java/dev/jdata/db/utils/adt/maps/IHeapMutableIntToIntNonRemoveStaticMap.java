package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableIntToIntNonRemoveStaticMap extends IMutableIntToIntNonRemoveStaticMap {

    public static IHeapMutableIntToIntNonRemoveStaticMap create(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableIntToIntNonRemoveNonBucketMap(AllocationType.HEAP, initialCapacity);
    }
}
