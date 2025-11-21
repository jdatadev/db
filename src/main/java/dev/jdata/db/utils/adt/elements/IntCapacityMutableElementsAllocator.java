package dev.jdata.db.utils.adt.elements;

import java.util.function.IntFunction;

import dev.jdata.db.utils.allocators.IntCapacityMutableInstanceAllocator;

public abstract class IntCapacityMutableElementsAllocator<T extends IMutableElements, U extends BaseADTElements<?, ?, ?> & ICapacity, V>

        extends IntCapacityMutableInstanceAllocator<T, U, V> {

    protected IntCapacityMutableElementsAllocator(IntFunction<V> createElements) {
        super(createElements, ICapacity::intCapacity);
    }
}
