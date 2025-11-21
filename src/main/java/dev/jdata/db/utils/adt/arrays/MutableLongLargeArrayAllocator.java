package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;

@Deprecated // necessary?
abstract class MutableLongLargeArrayAllocator<T extends IMutableLongLargeArray, U extends MutableLongLargeArray & IMutableLongLargeArray>

        extends MutableOneDimensionalLargeArrayInstanceAllocator<T, U, long[], ILongIterableElementsView>
        implements IMutableLongLargeArrayAllocator<T> {

    MutableLongLargeArrayAllocator() {
        super(null);
    }
}
