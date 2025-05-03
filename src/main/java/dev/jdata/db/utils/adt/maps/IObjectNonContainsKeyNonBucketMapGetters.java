package dev.jdata.db.utils.adt.maps;

public interface IObjectNonContainsKeyNonBucketMapGetters<K, V> extends IMapGetters {

    V get(K key);
}
