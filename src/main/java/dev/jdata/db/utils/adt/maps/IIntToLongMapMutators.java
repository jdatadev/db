package dev.jdata.db.utils.adt.maps;

public interface IIntToLongMapMutators extends IMapMutators {

    long put(int key, long  value, long defaultPreviousValue);

    default void put(int key, long  value) {

        put(key, value, -1L);
    }
}
