package dev.jdata.db.utils.adt.maps;

interface IIntToObjectDynamicMapGetters<V> extends IKeyValueDynamicMapGettersMarker {

    V get(int key, V defaultValue);

    default V get(int key) {

        return get(key, null);
    }
}
