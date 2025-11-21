package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.allocators.IntCapacityMutableInstanceAllocator;

abstract class BaseIntCapacityMutableArrayAllocator<T extends IMutableAnyDimensionalArray, U extends BaseOneDimensionalArray<V> & ICapacity, V>

        extends IntCapacityMutableInstanceAllocator<T, U, V> {

    BaseIntCapacityMutableArrayAllocator(IntFunction<V> createElements) {
        super(createElements, ICapacity::intCapacity);
    }
}
