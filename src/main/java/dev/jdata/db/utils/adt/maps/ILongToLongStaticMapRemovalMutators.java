package dev.jdata.db.utils.adt.maps;

public interface ILongToLongStaticMapRemovalMutators extends IKeyValueStaticMapRemovalMutators {

    long removeAndReturnPrevious(long key);
}
