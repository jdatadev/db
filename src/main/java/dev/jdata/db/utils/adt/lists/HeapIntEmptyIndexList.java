package dev.jdata.db.utils.adt.lists;

final class HeapIntEmptyIndexList extends IntEmptyIndexList implements IHeapIntIndexList {

    private static final HeapIntEmptyIndexList INSTANCE = new HeapIntEmptyIndexList();

    static IHeapIntIndexList empty() {

        return INSTANCE;
    }
}
