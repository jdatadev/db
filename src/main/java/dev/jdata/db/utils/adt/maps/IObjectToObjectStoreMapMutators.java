package dev.jdata.db.utils.adt.maps;

interface IObjectToObjectStoreMapMutators<K, V> extends IStoreMapMutatorsMarker {

    V put(K key, V value, V defaultPreviousValue);

    default void put(K key, V value) {

        put(key, value, null);
    }
}
