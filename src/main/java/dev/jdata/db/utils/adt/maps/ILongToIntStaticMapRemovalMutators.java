package dev.jdata.db.utils.adt.maps;

public interface ILongToIntStaticMapRemovalMutators extends IKeyValueStaticMapRemovalMutatorsMarker {

    int removeAndReturnPrevious(long key);
}
