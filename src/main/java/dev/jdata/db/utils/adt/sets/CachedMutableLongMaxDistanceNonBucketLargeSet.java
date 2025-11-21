package dev.jdata.db.utils.adt.sets;

final class CachedMutableLongMaxDistanceNonBucketLargeSet extends MutableLongMaxDistanceNonBucketLargeSet implements ICachedMutableLongLargeSet {

    static CachedMutableLongMaxDistanceNonBucketLargeSet create(AllocationType allocationType, long initialCapacity) {

        return instantiateWithOuterExponentInnerExponent(allocationType, AllocationMechanism.CACHE, initialCapacity, CachedMutableLongMaxDistanceNonBucketLargeSet::new);
    }

    private CachedMutableLongMaxDistanceNonBucketLargeSet(AllocationType allocationType, int outerCapacityExponent, int innerCapacityExponent) {
        super(allocationType, outerCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, DEFAULT_LOAD_FACTOR);
    }
}
