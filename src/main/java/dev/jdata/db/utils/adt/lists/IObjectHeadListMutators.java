package dev.jdata.db.utils.adt.lists;

interface IObjectHeadListMutators<T> extends IHeadListMutators {

    void addHead(T instance);

    T removeHeadAndReturnValue();
}
