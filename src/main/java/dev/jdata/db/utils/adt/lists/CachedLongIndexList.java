package dev.jdata.db.utils.adt.lists;

final class CachedLongIndexList extends LongIndexList implements ICachedLongIndexList {

    private static final CachedLongIndexList emptyList = new CachedLongIndexList(AllocationType.HEAP);

    static CachedLongIndexList empty() {

        return emptyList;
    }

    static CachedLongIndexList createEmptyValuesInitializable(AllocationType allocationType) {

        AllocationType.checkIsCached(allocationType);

        return new CachedLongIndexList(allocationType);
    }

    static CachedLongIndexList withArray(AllocationType allocationType, long[] values, int numElements) {

        checkWithArrayParameters(allocationType, AllocationMechanism.CACHE, values, numElements);

        return new CachedLongIndexList(allocationType, values, numElements);
    }

    private CachedLongIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private CachedLongIndexList(AllocationType allocationType, long value) {
        super(allocationType, value);
    }

    private CachedLongIndexList(AllocationType allocationType, long[] values, int numElements) {
        super(allocationType, values, numElements);
    }
}
