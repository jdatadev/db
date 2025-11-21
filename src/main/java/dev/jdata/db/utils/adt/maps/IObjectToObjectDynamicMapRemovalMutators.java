package dev.jdata.db.utils.adt.maps;

interface IObjectToObjectDynamicMapRemovalMutators<K, V> extends IKeyValueDynamicMapRemovalMutatorsMarker {

    V removeAndReturnPrevious(K key, V defaultValue);
}
