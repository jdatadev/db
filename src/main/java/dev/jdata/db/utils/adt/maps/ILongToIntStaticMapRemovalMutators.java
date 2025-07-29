package dev.jdata.db.utils.adt.maps;

public interface ILongToIntStaticMapRemovalMutators extends IKeyValueStaticMapRemovalMutators {

    int removeAndReturnPrevious(long key);
}
