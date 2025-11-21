package dev.jdata.db.utils.adt.lists;

public interface IIndexList<T> extends IBaseIndexList<T> {

    IHeapIndexList<T> toHeapAllocated();
}
