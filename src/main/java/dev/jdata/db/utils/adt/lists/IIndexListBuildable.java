package dev.jdata.db.utils.adt.lists;

public interface IIndexListBuildable<T> extends IListBuildable<T> {

    IIndexList<T> build();
}
