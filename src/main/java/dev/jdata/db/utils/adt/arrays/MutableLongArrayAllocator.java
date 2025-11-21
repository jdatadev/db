package dev.jdata.db.utils.adt.arrays;

@Deprecated // necessary?
abstract class MutableLongArrayAllocator<T extends IMutableLongArray, U extends BaseLongArray & IMutableLongArray>

        extends MutableOneDimensionalArrayInstanceAllocator<T, U, long[]>
        implements IMutableLongArrayAllocator<T> {

    MutableLongArrayAllocator() {
        super(long[]::new);
    }
}
