package dev.jdata.db.utils.adt.maps;

interface IIntToLongDynamicMapRemovalMutators extends IKeyValueDynamicMapRemovalMutatorsMarker {

    long removeAndReturnPrevious(int key, long defaultValue);
}
