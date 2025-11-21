package dev.jdata.db.utils.adt.maps;

interface IIntToIntDynamicMapRemovalMutators extends IKeyValueDynamicMapRemovalMutatorsMarker {

    int removeAndReturnPrevious(int key, int defaultValue);
}
