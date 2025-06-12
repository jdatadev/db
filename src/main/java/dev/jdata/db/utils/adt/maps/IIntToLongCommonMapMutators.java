package dev.jdata.db.utils.adt.maps;

interface IIntToLongCommonMapMutators extends ICommonMapMutators {

    long put(int key, long  value, long defaultPreviousValue);

    default void put(int key, long  value) {

        put(key, value, -1L);
    }
}
