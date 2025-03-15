package dev.jdata.db.utils.adt.lists;

public interface IMutableIndexList<T> extends IIndexList<T>, ITailListMutators<T> {

    void addHead(T instance);
}
