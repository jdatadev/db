package dev.jdata.db.utils.adt.maps;

public interface ILongToObjectStaticMapRemovalMutators<T> extends IKeyValueStaticMapRemovalMutators {

    T removeAndReturnPrevious(long key);
}
