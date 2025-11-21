package dev.jdata.db.utils.adt.sets;

abstract class MutableIntSetAllocator<T extends IMutableIntSet, U extends BaseIntCapacityExponentSet<?, ?> & IMutableIntSet>

        extends BaseIntCapacityMutableSetAllocator<T, U, int[]>
        implements IMutableIntSetAllocator<T> {

    MutableIntSetAllocator() {
        super(int[]::new);
    }
}
