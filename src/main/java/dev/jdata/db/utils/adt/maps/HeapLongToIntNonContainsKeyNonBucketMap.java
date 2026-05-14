package dev.jdata.db.utils.adt.maps;

final class HeapLongToIntNonContainsKeyNonBucketMap extends LongToIntNonContainsKeyNonBucketMap<HeapLongToIntNonContainsKeyNonBucketMap> implements IHeapLongToIntStaticMap {

    static HeapLongToIntNonContainsKeyNonBucketMap withMakeElementsFrom(AllocationType allocationType,
            MutableLongToIntNonRemoveNonBucketMap<?> toInitializeFrom) {

        checkWithMakeElementsFrom(allocationType, AllocationMechanism.HEAP, toInitializeFrom);

        return new HeapLongToIntNonContainsKeyNonBucketMap(allocationType, toInitializeFrom, null);
    }

    private HeapLongToIntNonContainsKeyNonBucketMap(AllocationType allocationType, BaseLongToIntNonBucketMap<?> toInitializeFrom, Void disambiguate) {
        super(allocationType, toInitializeFrom, disambiguate);
    }
}
