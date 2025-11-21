package dev.jdata.db.utils.adt.elements;

import java.util.function.LongFunction;

import dev.jdata.db.utils.allocators.LongCapacityMutableInstanceAllocator;

public abstract class LongCapacityMutableElementsAllocator<T extends IMutableElements, U extends BaseADTElements<?, ?, ?> & ICapacity, V>

        extends LongCapacityMutableInstanceAllocator<T, U, V>
        implements IMutableElementsAllocator<T> {

    protected LongCapacityMutableElementsAllocator(LongFunction<V> createElements) {
        super(createElements, ICapacity::getCapacity);
    }
}
