package dev.jdata.db.utils.adt.maps;

interface ILongToObjectDynamicMapRemovalMutators<T> extends IKeyValueDynamicMapRemovalMutatorsMarker {

    T removeAndReturnPrevious(long key, T defaultValue);
}
