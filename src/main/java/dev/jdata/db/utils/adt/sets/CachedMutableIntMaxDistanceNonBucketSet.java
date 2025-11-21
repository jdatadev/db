package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;

final class CachedMutableIntMaxDistanceNonBucketSet extends MutableIntMaxDistanceNonBucketSet implements ICachedMutableIntSet {

    static CachedMutableIntMaxDistanceNonBucketSet create(AllocationType allocationType, int initialCapacity) {

        return instantiateWithCapacityExponent(allocationType, AllocationMechanism.CACHE, initialCapacity, CachedMutableIntMaxDistanceNonBucketSet::new);
    }

    static CachedMutableIntMaxDistanceNonBucketSet copyToMutable(AllocationType allocationType, IIntIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(allocationType, AllocationMechanism.CACHE, mutableFrom);

        return new CachedMutableIntMaxDistanceNonBucketSet(allocationType, mutableFrom);
    }

    private CachedMutableIntMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }

    private CachedMutableIntMaxDistanceNonBucketSet(AllocationType allocationType, IIntIterableElementsView mutableFrom) {
        super(allocationType, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, mutableFrom);
    }
}
