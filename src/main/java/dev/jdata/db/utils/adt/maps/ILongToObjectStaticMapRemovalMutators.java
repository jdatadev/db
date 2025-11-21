package dev.jdata.db.utils.adt.maps;

public interface ILongToObjectStaticMapRemovalMutators<T> extends IKeyValueStaticMapRemovalMutatorsMarker {

    T removeAndReturnPrevious(long key);
}
