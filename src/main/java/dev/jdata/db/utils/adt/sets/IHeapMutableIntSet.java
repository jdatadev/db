package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableIntSet extends IMutableIntSet, IHeapContainsMarker {

    public static IHeapMutableIntSet create(int initialCapacity) {

        return HeapMutableIntMaxDistanceNonBucketSet.create(AllocationType.HEAP, initialCapacity);
    }
}
