package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;

public interface IIndexList<T> extends IIndexListGetters<T> {

    public static <T> IIndexList<T> empty() {

        return IndexList.empty();
    }

    <U extends MutableIndexList<T>> U copyToMutable(IndexListAllocator<T, ? extends IndexList<T>, ?, U> indexListAllocator);
}
