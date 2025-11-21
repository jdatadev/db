package dev.jdata.db.utils.adt.elements;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.capacity.ICapacity;
import dev.jdata.db.utils.allocators.IntCapacityMutableInstanceAllocator;

public abstract class IntCapacityMutableElementsAllocator<

                INTERFACE_MUTABLE extends IMutableElements,
                CLASS_MUTABLE extends BaseADTElements<?, ?, ?> & ICapacity,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends IntCapacityMutableInstanceAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, ELEMENTS, MUTABLE_FROM> {

    protected IntCapacityMutableElementsAllocator(IntFunction<ELEMENTS> createElements) {
        super(createElements, ICapacity::intCapacity);
    }
}
