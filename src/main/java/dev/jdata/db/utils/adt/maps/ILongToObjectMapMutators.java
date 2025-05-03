package dev.jdata.db.utils.adt.maps;

public interface ILongToObjectMapMutators<T> extends IMapMutators {

    T put(long key, T value, T defaultPreviousValue);

    default void put(long key, T value) {

        put(key, value, null);
    }
}
