package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;

final class CachedMutableIntIndexList extends MutableIntIndexList implements ICachedMutableIntIndexList {

    static CachedMutableIntIndexList create(AllocationType allocationType, int initialCapacity) {

        checkMutableCreateParameters(allocationType, AllocationMechanism.CACHE, initialCapacity);

        return new CachedMutableIntIndexList(allocationType, initialCapacity);
    }

    static CachedMutableIntIndexList copyToMutable(AllocationType allocationType, IIntIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(allocationType, AllocationMechanism.CACHE, mutableFrom);

        return new CachedMutableIntIndexList(allocationType, mutableFrom);
    }

    private CachedMutableIntIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    private CachedMutableIntIndexList(AllocationType allocationType, IIntIterableElementsView mutableFrom) {
        super(allocationType, mutableFrom);
    }
}
