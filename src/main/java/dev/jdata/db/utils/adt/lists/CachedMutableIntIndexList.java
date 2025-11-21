package dev.jdata.db.utils.adt.lists;

final class CachedMutableIntIndexList extends MutableIntIndexList implements ICachedMutableIntIndexList {

    CachedMutableIntIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    CachedMutableIntIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }
}
