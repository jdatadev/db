package dev.jdata.db.utils.adt.lists;

public interface IIndexList<T> extends IBaseObjectIndexList<T> {

    IHeapIndexList<T> toHeapAllocated();
}
