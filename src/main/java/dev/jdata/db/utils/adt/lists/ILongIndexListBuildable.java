package dev.jdata.db.utils.adt.lists;

public interface ILongIndexListBuildable<T extends LongIndexList, U extends ILongIndexListBuildable<T, U>> extends ILongListBuildable<T, U> {

    T build();
}
