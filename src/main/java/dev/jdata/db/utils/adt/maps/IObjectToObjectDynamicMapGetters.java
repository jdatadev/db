package dev.jdata.db.utils.adt.maps;

interface IObjectToObjectDynamicMapGetters<K, V> extends IKeyValueDynamicMapGettersMarker {

    V get(K key, V defaultValue);
}
