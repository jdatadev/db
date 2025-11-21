package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableLongLargeSet extends IMutableLongLargeSet, IHeapContainsMarker {

    public static IMutableLongLargeSet create(int initialCapacityExponent) {

        Checks.isLongCapacityExponent(initialCapacityExponent);

        return new HeapMutableLongLargeMaxDistanceNonBucketSet(AllocationType.HEAP, initialCapacityExponent);
    }
}
