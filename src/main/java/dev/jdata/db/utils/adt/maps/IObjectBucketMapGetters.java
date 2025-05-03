package dev.jdata.db.utils.adt.maps;

public interface IObjectBucketMapGetters<K, V> extends IMapGetters {

    V get(K key, V defaultValue);
}
