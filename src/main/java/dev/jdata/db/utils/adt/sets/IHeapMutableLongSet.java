package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapMutableLongSet extends IMutableLongSet, IHeapContainsMarker {

    public static IHeapMutableLongSet create() {

        return HeapMutableLongMaxDistanceNonBucketSet.create(AllocationType.HEAP);
    }

    public static IHeapMutableLongSet create(int initialCapacity) {

        return HeapMutableLongMaxDistanceNonBucketSet.create(AllocationType.HEAP, initialCapacity);
    }

    public static IHeapMutableLongSet of(long value) {

        final IHeapMutableLongSet result = HeapMutableLongMaxDistanceNonBucketSet.create(AllocationType.HEAP, 1);

        result.addUnordered(value);

        return result;
    }
}
