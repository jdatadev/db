package dev.jdata.db.utils.adt.sets;

abstract class MutableLongSetAllocator<T extends IMutableLongSet, U extends BaseIntCapacityExponentSet<?, ?> & IMutableLongSet>

        extends BaseIntCapacityMutableSetAllocator<T, U, long[]>
        implements IMutableLongSetAllocator<T> {

    MutableLongSetAllocator() {
        super(long[]::new);
    }
}
