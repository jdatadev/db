package dev.jdata.db.utils.adt.lists;

final class HeapLongEmptyIndexList extends LongEmptyIndexList implements IHeapLongIndexList {

    private static final HeapLongEmptyIndexList INSTANCE = new HeapLongEmptyIndexList();

    static IHeapLongIndexList empty() {

        return INSTANCE;
    }
}
