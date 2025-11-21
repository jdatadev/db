package dev.jdata.db.utils.adt.maps;

interface ILongToIntStoreMapMutators extends IStoreMapMutatorsMarker {

    int put(long key, int value, int defaultPreviousValue);

    default void put(long key, int value) {

        put(key, value, -1);
    }
}
