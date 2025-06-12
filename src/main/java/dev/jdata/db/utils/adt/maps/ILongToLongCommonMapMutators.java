package dev.jdata.db.utils.adt.maps;

interface ILongToLongCommonMapMutators extends ICommonMapMutators {

    long put(long key, long value, long defaultPreviousValue);

    default void put(long key, long value) {

        put(key, value, -1L);
    }
}
