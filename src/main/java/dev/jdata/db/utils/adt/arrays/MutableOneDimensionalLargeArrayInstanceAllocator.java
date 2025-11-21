package dev.jdata.db.utils.adt.arrays;

import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.elements.LongCapacityMutableElementsAllocator;

abstract class MutableOneDimensionalLargeArrayInstanceAllocator<

                T extends IMutableOneDimensionalArray,
                U extends BaseOneDimensionalLargeArray<?, ?> & IMutableOneDimensionalArray,
                V>

        extends LongCapacityMutableElementsAllocator<T, U, V>
        implements IMutableOneDimensionalLargeArrayInstanceAllocator<T> {

    MutableOneDimensionalLargeArrayInstanceAllocator(LongFunction<V> createElements) {
        super(createElements);
    }
}
