package dev.jdata.db.utils.adt.maps;

public interface ILongToLongStaticMapRemovalMutators extends IKeyValueStaticMapRemovalMutatorsMarker {

    long removeAndReturnPrevious(long key);
}
