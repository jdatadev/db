package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableLongToIntWithRemoveStaticMap extends IMutableLongToIntWithRemoveStaticMap {

    public static IHeapMutableLongToIntWithRemoveStaticMap create(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableLongToIntWithRemoveNonBucketMap(AllocationType.HEAP, initialCapacity);
    }
}
