package dev.jdata.db.utils.adt.maps;

public interface ILongToObjectMapRemovalMutators<T> {

    T removeAndReturnPrevious(long key, T defaultValue);
}
