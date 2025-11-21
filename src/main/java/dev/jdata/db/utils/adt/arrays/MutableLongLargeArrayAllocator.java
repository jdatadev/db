package dev.jdata.db.utils.adt.arrays;

@Deprecated // necessary?
abstract class MutableLongLargeArrayAllocator<T extends IMutableLongLargeArray, U extends MutableLongLargeArray & IMutableLongLargeArray>

        extends MutableOneDimensionalLargeArrayInstanceAllocator<T, U, long[]>
        implements IMutableLongLargeArrayAllocator<T> {

    MutableLongLargeArrayAllocator() {
        super(null);
    }
}
