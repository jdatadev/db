package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongLargeSet extends IMutableLongLargeSet, IHeapContainsMarker {

    public static IMutableLongLargeSet create(long initialCapacity) {

        return HeapMutableLongMaxDistanceNonBucketLargeSet.create(AllocationType.HEAP, initialCapacity);
    }
}
