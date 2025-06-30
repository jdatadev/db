package dev.jdata.db.utils.adt.lists;

public interface IIndexListBuildable<T, U extends IIndexList<T>, V extends IIndexListBuildable<T, U, V>> extends IListBuildable<T, V> {

    U build();
}
