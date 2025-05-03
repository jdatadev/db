package dev.jdata.db.utils.adt.maps;

public interface ILongToLongMapMutators extends IMapMutators {

    long put(long key, long value, long defaultPreviousValue);

    default void put(long key, long value) {

        put(key, value, -1L);
    }
}
