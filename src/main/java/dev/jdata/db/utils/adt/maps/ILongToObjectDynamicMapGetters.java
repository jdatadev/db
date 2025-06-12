package dev.jdata.db.utils.adt.maps;

interface ILongToObjectDynamicMapGetters<T> extends IDynamicMapGetters {

    T get(long key, T defaultValue);

    default T get(long key) {

        return get(key, null);
    }
}
