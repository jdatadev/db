package dev.jdata.db.utils.adt.maps;

interface ILongToIntCommonMapMutators extends ICommonMapMutators {

    int put(long key, int value, int defaultPreviousValue);

    default void put(long key, int value) {

        put(key, value, -1);
    }
}
