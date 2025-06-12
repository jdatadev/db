package dev.jdata.db.utils.adt.maps;

interface IObjectStaticMapGetters<K, V> extends IStaticMapGetters {

    V get(K key);
}
