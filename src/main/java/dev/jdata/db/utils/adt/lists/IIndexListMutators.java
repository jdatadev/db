package dev.jdata.db.utils.adt.lists;

public interface IIndexListMutators<T> extends ITailListMutators<T> {

    void addHead(T instance);
}
