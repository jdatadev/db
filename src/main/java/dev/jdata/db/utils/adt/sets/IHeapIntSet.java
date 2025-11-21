package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapIntSet extends IIntSet, IHeapContainsMarker {

    public static IHeapIntSet of(int ... values) {

        return new HeapIntMaxDistanceNonBucketSet(AllocationType.HEAP, values);
    }
}
