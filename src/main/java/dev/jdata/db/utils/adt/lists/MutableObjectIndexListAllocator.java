package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

abstract class MutableObjectIndexListAllocator<T, U extends IMutableIndexList<T>, V extends MutableObjectIndexList<T>>

        extends BaseMutableIndexListAllocator<U, V, T[]>
        implements IMutableIndexListAllocator<T, U> {

    MutableObjectIndexListAllocator(IntFunction<T[]> createElementsArray) {
        super(createElementsArray);
    }
}
