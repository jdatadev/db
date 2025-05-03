package dev.jdata.db.utils.adt.maps;

public interface IIntToObjectBucketMapMutators<T> extends IMapMutators {

    T removeAndReturnPrevious(int key, T defaultValue);

    boolean remove(int key);
}
