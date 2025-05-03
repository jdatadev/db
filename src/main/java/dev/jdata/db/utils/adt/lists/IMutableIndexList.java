package dev.jdata.db.utils.adt.lists;

import java.util.Comparator;

public interface IMutableIndexList<T> extends IIndexListGetters<T>, IIndexListMutators<T> {

    void set(long index, T instance);

    void sort(Comparator<? super T> comparator);
}
