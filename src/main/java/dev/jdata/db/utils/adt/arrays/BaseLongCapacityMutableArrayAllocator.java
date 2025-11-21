package dev.jdata.db.utils.adt.arrays;

import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.allocators.LongCapacityMutableInstanceAllocator;

abstract class BaseLongCapacityMutableArrayAllocator<T extends IMutableOneDimensionalArray, U extends BaseOneDimensionalLargeArray<?, ?> & ICapacity, V>

        extends LongCapacityMutableInstanceAllocator<T, U, V> {

    BaseLongCapacityMutableArrayAllocator(LongFunction<V> createElements) {
        super(createElements, ICapacity::getCapacity);
    }
}
