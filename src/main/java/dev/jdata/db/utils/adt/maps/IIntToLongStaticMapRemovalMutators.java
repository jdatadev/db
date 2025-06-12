package dev.jdata.db.utils.adt.maps;

interface IIntToLongStaticMapRemovalMutators extends IKeyValueStaticMapRemovalMutators {

    long removeAndReturnPrevious(int key);
}
