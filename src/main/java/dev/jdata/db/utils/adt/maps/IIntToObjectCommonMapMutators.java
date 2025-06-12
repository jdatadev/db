package dev.jdata.db.utils.adt.maps;

interface IIntToObjectCommonMapMutators<T> extends ICommonMapMutators {

    T put(int key, T value, T defaultPreviousValue);

    default void put(int key, T value) {

        put(key, value, null);
    }
}
