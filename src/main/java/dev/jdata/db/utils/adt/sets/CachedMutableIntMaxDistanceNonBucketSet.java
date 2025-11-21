package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.checks.Checks;

final class CachedMutableIntMaxDistanceNonBucketSet extends MutableIntMaxDistanceNonBucketSet implements ICachedMutableIntSet {

    static CachedMutableIntMaxDistanceNonBucketSet create(AllocationType allocationType, int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return new CachedMutableIntMaxDistanceNonBucketSet(allocationType, CapacityExponents.computeIntCapacityExponent(initialCapacity));
    }

    private CachedMutableIntMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);
    }

    private CachedMutableIntMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }
}
