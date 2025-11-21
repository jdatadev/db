package dev.jdata.db.utils.adt.lists;

interface IMutableIndexList<

                T,
                IMMUTABLE extends IIndexList<T>,
                ALLOCATOR extends IIndexListAllocator<T, IMMUTABLE, ?>>

        extends IBaseMutableIndexList<T, IMMUTABLE, ALLOCATOR> {

}
