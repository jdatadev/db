package dev.jdata.db.utils.adt.lists;

final class CachedMutableLongIndexList extends MutableLongIndexList implements ICachedMutableLongIndexList {

    CachedMutableLongIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    CachedMutableLongIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }
}
