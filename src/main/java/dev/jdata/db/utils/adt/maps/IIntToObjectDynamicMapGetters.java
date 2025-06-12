package dev.jdata.db.utils.adt.maps;

interface IIntToObjectDynamicMapGetters<T> extends IDynamicMapGetters {

    T get(int key, T defaultValue);

    default T get(int key) {

        return get(key, null);
    }
}
