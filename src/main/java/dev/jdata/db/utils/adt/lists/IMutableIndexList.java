package dev.jdata.db.utils.adt.lists;

import java.util.Comparator;

import dev.jdata.db.utils.adt.elements.ICapacity;

public interface IMutableIndexList<T> extends IIndexListGetters<T>, ICapacity, IIndexListMutators<T> {

    void set(long index, T instance);

    void sort(Comparator<? super T> comparator);
}
