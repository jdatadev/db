package dev.jdata.db.utils.adt.maps;

interface IObjectToObjectBaseStaticMapGetters<K, V> extends IKeyValueBaseStaticMapGettersMarker {

    V get(K key);
}
