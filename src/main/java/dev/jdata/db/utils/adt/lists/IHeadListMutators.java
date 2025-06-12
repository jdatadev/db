package dev.jdata.db.utils.adt.lists;

public interface IHeadListMutators<T> {

    void addHead(T instance);

    T removeHead();
    void removeHead(long numElements);
}
