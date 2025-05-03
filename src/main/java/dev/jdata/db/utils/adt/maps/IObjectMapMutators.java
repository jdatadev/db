package dev.jdata.db.utils.adt.maps;

public interface IObjectMapMutators<K, V> extends IMapMutators {

    V put(K key, V value, V defaultPreviousValue);

    default void put(K key, V value) {

        put(key, value, null);
    }
}
