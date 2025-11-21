package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableLongSet extends IMutableLongSet {

    public static IHeapMutableLongSet create() {

        return new HeapMutableLongMaxDistanceNonBucketSet(AllocationType.HEAP);
    }

    public static IHeapMutableLongSet create(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableLongMaxDistanceNonBucketSet(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(initialCapacity));
    }

    public static IHeapMutableLongSet of(long value) {

        final IHeapMutableLongSet result = new HeapMutableLongMaxDistanceNonBucketSet(AllocationType.HEAP, CapacityExponents.computeIntCapacityExponent(1L));

        result.addUnordered(value);

        return result;
    }
}
