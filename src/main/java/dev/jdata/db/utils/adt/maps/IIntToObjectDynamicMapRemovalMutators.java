package dev.jdata.db.utils.adt.maps;

interface IIntToObjectDynamicMapRemovalMutators<T> extends IKeyValueDynamicMapRemovalMutators {

    T removeAndReturnPrevious(int key, T defaultValue);
}
