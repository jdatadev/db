package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IntCapacityMutableElementsAllocator;

abstract class MutableOneDimensionalArrayInstanceAllocator<T extends IMutableOneDimensionalArray, U extends BaseOneDimensionalArray<?> & IMutableOneDimensionalArray, V>

        extends IntCapacityMutableElementsAllocator<T, U, V>
        implements IMutableOneDimensionalArrayInstanceAllocator<T> {

    MutableOneDimensionalArrayInstanceAllocator(IntFunction<V> createElements) {
        super(createElements);
    }
}
