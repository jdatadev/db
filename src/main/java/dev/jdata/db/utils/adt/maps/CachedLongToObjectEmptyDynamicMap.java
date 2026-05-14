package dev.jdata.db.utils.adt.maps;

final class CachedLongToObjectEmptyDynamicMap<V> extends LongToObjectEmptyDynamicMap<V> implements ICachedLongToObjectDynamicMap<V> {

    private static final CachedLongToObjectEmptyDynamicMap<?> INSTANCE = new CachedLongToObjectEmptyDynamicMap<>();

    @SuppressWarnings("unchecked")
    static <V> ICachedLongToObjectDynamicMap<V> empty() {

        return (ICachedLongToObjectDynamicMap<V>)INSTANCE;
    }

    private CachedLongToObjectEmptyDynamicMap() {

    }
}
