package dev.jdata.db.utils.adt.lists;

final class CachedIntIndexList extends IntIndexList implements ICachedIntIndexList {

    private static final CachedIntIndexList emptyList = new CachedIntIndexList(AllocationType.HEAP);

    static CachedIntIndexList empty() {

        return emptyList;
    }

    static CachedIntIndexList createEmptyValuesInitializable(AllocationType allocationType) {

        AllocationType.checkIsCached(allocationType);

        return new CachedIntIndexList(allocationType);
    }

    static CachedIntIndexList withArray(AllocationType allocationType, int[] values, int numElements) {

        checkWithArrayParameters(allocationType, AllocationMechanism.CACHE, values, numElements);

        return new CachedIntIndexList(allocationType, values, numElements);
    }

    private CachedIntIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    private CachedIntIndexList(AllocationType allocationType, int value) {
        super(allocationType, value);
    }

    private CachedIntIndexList(AllocationType allocationType, int[] values, int numElements) {
        super(allocationType, values, numElements);
    }
}
