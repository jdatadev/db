package dev.jdata.db.utils.adt.maps;

public interface IIntToObjectBucketMapGetters<T> extends IMapGetters {

    T get(int key, T defaultValue);

    default T get(int key) {

        return get(key, null);
    }
}
