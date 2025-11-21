package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapLongSet extends ILongSet, IHeapContainsMarker {

    public static LongBucketSet of(long ... values) {

        return new HeapLongBucketSet(AllocationType.HEAP, values);
    }
}
