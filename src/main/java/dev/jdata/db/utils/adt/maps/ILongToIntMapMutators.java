package dev.jdata.db.utils.adt.maps;

public interface ILongToIntMapMutators extends IMapMutators {

    int put(long key, int value, int defaultPreviousValue);

    default void put(long key, int value) {

        put(key, value, -1);
    }
}
