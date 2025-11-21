package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableIntSet extends IMutableIntSet {

    public static IHeapMutableIntSet create(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return HeapMutableIntMaxDistanceNonBucketSet.create(AllocationType.HEAP, initialCapacity);
    }
}
