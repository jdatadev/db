package dev.jdata.db.utils.adt.lists;

final class HeapMutableIntIndexList extends MutableIntIndexList implements IHeapMutableIntIndexList {

    HeapMutableIntIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }
}
