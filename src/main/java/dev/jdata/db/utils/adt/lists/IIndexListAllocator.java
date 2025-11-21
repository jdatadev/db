package dev.jdata.db.utils.adt.lists;

public interface IIndexListAllocator<

                T,
                IMMUTABLE extends IIndexList<T>,
                MUTABLE extends IMutableIndexList<T>,
                BUILDER extends IIndexListBuilder<T, IMMUTABLE, ?>>

        extends IBaseObjectIndexListAllocator<T, IMMUTABLE, MUTABLE, BUILDER> {

}
