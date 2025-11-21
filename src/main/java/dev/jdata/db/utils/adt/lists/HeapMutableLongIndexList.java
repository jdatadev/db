package dev.jdata.db.utils.adt.lists;

final class HeapMutableLongIndexList extends MutableLongIndexList implements IHeapMutableLongIndexList {

    HeapMutableLongIndexList(AllocationType allocationType, int initialCapacity) {
        super(allocationType, initialCapacity);
    }
}
