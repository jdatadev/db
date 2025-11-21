package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;

final class CachedMutableLongIndexList extends MutableLongIndexList implements ICachedMutableLongIndexList {

    static CachedMutableLongIndexList create(AllocationType allocationType, int initialCapacity) {

        checkMutableCreateParameters(allocationType, AllocationMechanism.CACHE, initialCapacity);

        return new CachedMutableLongIndexList(allocationType, initialCapacity);
    }

    static CachedMutableLongIndexList copyToMutable(AllocationType allocationType, ILongIterableElementsView mutableFrom) {

        checkCopyToMutableParameters(allocationType, AllocationMechanism.CACHE, mutableFrom);

        return new CachedMutableLongIndexList(allocationType, mutableFrom);
    }

    private CachedMutableLongIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }

    private CachedMutableLongIndexList(AllocationType allocationType, ILongIterableElementsView mutableFrom) {
        super(allocationType, mutableFrom);
    }
}
