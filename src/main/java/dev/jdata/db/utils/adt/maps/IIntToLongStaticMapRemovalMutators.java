package dev.jdata.db.utils.adt.maps;

interface IIntToLongStaticMapRemovalMutators extends IKeyValueStaticMapRemovalMutatorsMarker {

    long removeAndReturnPrevious(int key);
}
