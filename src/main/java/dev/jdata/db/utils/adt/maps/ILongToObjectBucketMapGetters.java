package dev.jdata.db.utils.adt.maps;

public interface ILongToObjectBucketMapGetters<T > extends IMapGetters {

    T get(long key, T defaultValue);

    default T get(long key) {

        return get(key, null);
    }
}
