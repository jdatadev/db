package dev.jdata.db.utils.adt.maps;

public interface IIntToIntStaticMapRemovalMutators extends IKeyValueStaticMapRemovalMutators {

    int removeAndReturnPrevious(int key);
}
