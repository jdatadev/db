package dev.jdata.db.utils.adt.maps;

public interface IIntToObjectMapMutators<T> {

    T put(int key, T value, T defaultPreviousValue);

    default void put(int key, T value) {

        put(key, value, null);
    }
}
