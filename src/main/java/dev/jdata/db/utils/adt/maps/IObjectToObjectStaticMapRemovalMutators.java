package dev.jdata.db.utils.adt.maps;

interface IObjectToObjectStaticMapRemovalMutators<K, V> extends IKeyValueStaticMapRemovalMutatorsMarker {

    V removeAndReturnPrevious(K key);
}
