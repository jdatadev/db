package dev.jdata.db.utils.adt.maps;

interface IIntToObjectStaticMapRemovalMutators<T> extends IKeyValueStaticMapRemovalMutators {

    T removeAndReturnPrevious(int key);
}
