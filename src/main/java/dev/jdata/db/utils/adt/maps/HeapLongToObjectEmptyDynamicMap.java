package dev.jdata.db.utils.adt.maps;

final class HeapLongToObjectEmptyDynamicMap<V> extends LongToObjectEmptyDynamicMap<V> implements IHeapLongToObjectDynamicMap<V> {

    private static final HeapLongToObjectEmptyDynamicMap<?> INSTANCE = new HeapLongToObjectEmptyDynamicMap<>();

    @SuppressWarnings("unchecked")
    static <V> IHeapLongToObjectDynamicMap<V> empty() {

        return (IHeapLongToObjectDynamicMap<V>)INSTANCE;
    }

    private HeapLongToObjectEmptyDynamicMap() {

    }
}
