package dev.jdata.db.utils.adt.maps;

public interface IIntToIntStaticMapRemovalMutators extends IKeyValueStaticMapRemovalMutatorsMarker {

    int removeAndReturnPrevious(int key);
}
