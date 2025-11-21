package dev.jdata.db.utils.adt.sets;

final class HeapObjectEmptySet<T> extends ObjectEmptySet<T> implements IHeapSet<T> {

    private static final HeapObjectEmptySet<?> INSTANCE = new HeapObjectEmptySet<>();

    @SuppressWarnings("unchecked")
    static <T> IHeapSet<T> empty() {

        return (IHeapSet<T>)INSTANCE;
    }
}
