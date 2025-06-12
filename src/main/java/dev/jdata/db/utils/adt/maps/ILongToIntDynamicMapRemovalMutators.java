package dev.jdata.db.utils.adt.maps;

interface ILongToIntDynamicMapRemovalMutators extends IKeyValueDynamicMapRemovalMutators {

    int removeAndReturnPrevious(long key, int defaultValue);
}
