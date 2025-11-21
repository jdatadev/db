package dev.jdata.db.utils.adt.maps;

final class HeapLongToIntNonContainsKeyNonBucketMap extends LongToIntNonContainsKeyNonBucketMap<HeapLongToIntNonContainsKeyNonBucketMap> implements IHeapLongToIntStaticMap {

    static HeapLongToIntNonContainsKeyNonBucketMap withMakeElementsFrom(AllocationType allocationType, MutableLongToIntNonRemoveNonBucketMap<?> longToIntNonBucketMap) {

        checkWithMakeElementsFrom(allocationType, AllocationMechanism.HEAP, longToIntNonBucketMap);

        return new HeapLongToIntNonContainsKeyNonBucketMap(allocationType, longToIntNonBucketMap, null);
    }

    private HeapLongToIntNonContainsKeyNonBucketMap(AllocationType allocationType, BaseLongToIntNonBucketMap<?> toInitializeFrom, Void disambiguate) {
        super(allocationType, toInitializeFrom, disambiguate);
    }
}
