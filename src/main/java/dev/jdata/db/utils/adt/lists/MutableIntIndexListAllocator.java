package dev.jdata.db.utils.adt.lists;

abstract class MutableIntIndexListAllocator<T extends IMutableIntIndexList, U extends MutableIntIndexList>

        extends BaseMutableIndexListAllocator<T, U, int[]>
        implements IMutableIntIndexListAllocator<T> {

    MutableIntIndexListAllocator() {
        super(int[]::new);
    }
}
