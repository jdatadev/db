package dev.jdata.db.utils.adt.lists;

public interface IIndexList<T> extends IIndexListGetters<T> {

    public static <T> IIndexList<T> empty() {

        return IndexList.empty();
    }

    IMutableIndexList<T> copyToMutable();
}
