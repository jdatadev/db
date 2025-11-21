package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;

abstract class MutableObjectIndexListAllocator<T, U extends IMutableIndexList<T>, V extends MutableObjectIndexList<T>>

        extends BaseMutableIndexListAllocator<U, V, T[], IObjectIterableElementsView<T>>
        implements IMutableIndexListAllocator<T, U> {

    MutableObjectIndexListAllocator(IntFunction<T[]> createElementsArray) {
        super(createElementsArray);
    }
}
