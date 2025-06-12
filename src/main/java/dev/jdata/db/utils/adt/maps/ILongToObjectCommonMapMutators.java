package dev.jdata.db.utils.adt.maps;

interface ILongToObjectCommonMapMutators<T> extends ICommonMapMutators {

    T put(long key, T value, T defaultPreviousValue);

    default void put(long key, T value) {

        put(key, value, null);
    }
}
