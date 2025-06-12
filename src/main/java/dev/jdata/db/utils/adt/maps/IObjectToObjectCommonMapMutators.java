package dev.jdata.db.utils.adt.maps;

interface IObjectToObjectCommonMapMutators<K, V> extends ICommonMapMutators {

    V put(K key, V value, V defaultPreviousValue);

    default void put(K key, V value) {

        put(key, value, null);
    }
}
