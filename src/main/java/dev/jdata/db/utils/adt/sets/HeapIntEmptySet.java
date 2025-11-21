package dev.jdata.db.utils.adt.sets;

final class HeapIntEmptySet extends IntEmptySet implements IHeapIntSet {

    private static final HeapIntEmptySet INSTANCE = new HeapIntEmptySet();

    static IHeapIntSet empty() {

        return INSTANCE;
    }
}
