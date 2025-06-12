package dev.jdata.db.utils.adt.maps;

interface IObjectToObjectDynamicMapGetters<K, V> extends IDynamicMapGetters {

    V get(K key, V defaultValue);
}
