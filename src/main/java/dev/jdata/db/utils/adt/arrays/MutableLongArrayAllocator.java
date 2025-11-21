package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;

@Deprecated // necessary?
abstract class MutableLongArrayAllocator<T extends IMutableLongArray, U extends BaseLongArray & IMutableLongArray>

        extends MutableOneDimensionalArrayInstanceAllocator<T, U, long[], ILongIterableElementsView>
        implements IMutableLongArrayAllocator<T> {

    MutableLongArrayAllocator() {
        super(long[]::new);
    }
}
