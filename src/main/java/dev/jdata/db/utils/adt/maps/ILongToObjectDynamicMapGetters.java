package dev.jdata.db.utils.adt.maps;

interface ILongToObjectDynamicMapGetters<V> extends IKeyValueDynamicMapGettersMarker {

    V get(long key, V defaultValue);

    default V get(long key) {

        return get(key, null);
    }
}
