package dev.jdata.db.utils.adt.lists;

interface IInnerOuterNodeListInternal<T> extends INodeListInternal<T> {

    int getOuterIndex(long node);
    int getInnerIndex(long node);
}
