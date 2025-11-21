package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.Checks;

final class CachedMutableLongLargeMaxDistanceNonBucketSet extends MutableLongLargeMaxDistanceNonBucketSet implements ICachedMutableLongLargeSet {

    static CachedMutableLongLargeMaxDistanceNonBucketSet create(AllocationType allocationType, long initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new CachedMutableLongLargeMaxDistanceNonBucketSet(allocationType, CapacityExponents.computeLongCapacityExponent(initialCapacity));
    }

    private CachedMutableLongLargeMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }
}
