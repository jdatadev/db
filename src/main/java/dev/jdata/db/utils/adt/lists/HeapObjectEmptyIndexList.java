package dev.jdata.db.utils.adt.lists;

final class HeapObjectEmptyIndexList<T> extends ObjectEmptyIndexList<T> implements IHeapIndexList<T> {

    private static final HeapObjectEmptyIndexList<?> INSTANCE = new HeapObjectEmptyIndexList<>();

    @SuppressWarnings("unchecked")
    static <T> IHeapIndexList<T> empty() {

        return (IHeapIndexList<T>)INSTANCE;
    }

    @Override
    public IHeapIndexList<T> toHeapAllocated() {

        return this;
    }
}
