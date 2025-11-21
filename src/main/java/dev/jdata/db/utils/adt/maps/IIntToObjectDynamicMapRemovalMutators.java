package dev.jdata.db.utils.adt.maps;

interface IIntToObjectDynamicMapRemovalMutators<T> extends IKeyValueDynamicMapRemovalMutatorsMarker {

    T removeAndReturnPrevious(int key, T defaultValue);
}
