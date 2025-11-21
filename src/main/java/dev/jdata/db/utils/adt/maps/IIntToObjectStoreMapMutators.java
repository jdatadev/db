package dev.jdata.db.utils.adt.maps;

interface IIntToObjectStoreMapMutators<V> extends IStoreMapMutatorsMarker {

    V put(int key, V value, V defaultPreviousValue);

    default void put(int key, V value) {

        put(key, value, null);
    }
}
