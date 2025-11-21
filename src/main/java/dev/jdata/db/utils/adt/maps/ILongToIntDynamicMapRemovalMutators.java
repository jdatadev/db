package dev.jdata.db.utils.adt.maps;

interface ILongToIntDynamicMapRemovalMutators extends IKeyValueDynamicMapRemovalMutatorsMarker {

    int removeAndReturnPrevious(long key, int defaultValue);
}
