package dev.jdata.db.utils.adt.maps;

interface IIntToLongDynamicMapRemovalMutators extends IKeyValueDynamicMapRemovalMutators {

    long removeAndReturnPrevious(int key, long defaultValue);
}
