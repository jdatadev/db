package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.Checks;

final class HeapMutableIntMaxDistanceNonBucketSet extends MutableIntMaxDistanceNonBucketSet implements IHeapMutableIntSet {

    static HeapMutableIntMaxDistanceNonBucketSet create(AllocationType allocationType, int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new HeapMutableIntMaxDistanceNonBucketSet(allocationType, CapacityExponents.computeIntCapacityExponent(initialCapacity));
    }

    private HeapMutableIntMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
