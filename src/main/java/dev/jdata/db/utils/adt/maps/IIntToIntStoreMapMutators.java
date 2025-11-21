package dev.jdata.db.utils.adt.maps;

interface IIntToIntStoreMapMutators extends IStoreMapMutatorsMarker {

    int put(int key, int value, int defaultPreviousValue);

    default void put(int key, int value) {

        put(key, value, -1);
    }
}
