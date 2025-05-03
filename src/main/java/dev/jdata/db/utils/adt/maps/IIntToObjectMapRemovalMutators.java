package dev.jdata.db.utils.adt.maps;

public interface IIntToObjectMapRemovalMutators<T> extends IMapMutators {

    T removeAndReturnPrevious(int key, T defaultValue);
}
