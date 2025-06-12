package dev.jdata.db.utils.adt.maps;

interface IIntToIntDynamicMapRemovalMutators extends IKeyValueDynamicMapRemovalMutators {

    int removeAndReturnPrevious(int key, int defaultValue);
}
