package dev.jdata.db.utils.adt.lists;

public interface IIndexList<T> extends IList<T>, ITailList<T> {

    T get(long index);
}
