package dev.jdata.db.utils.adt.maps;

interface ILongToObjectDynamicMapRemovalMutators<T> extends IKeyValueDynamicMapRemovalMutators {

    T removeAndReturnPrevious(long key, T defaultValue);
}
