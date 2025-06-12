package dev.jdata.db.utils.adt.maps;

interface IObjectToObjectDynamicMapRemovalMutators<K, V> extends IKeyValueDynamicMapRemovalMutators {

    V removeAndReturnPrevious(K key, V defaultValue);
}
