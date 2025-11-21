package dev.jdata.db.utils.adt.elements;

import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.capacity.ICapacity;
import dev.jdata.db.utils.allocators.LongCapacityMutableInstanceAllocator;

public abstract class LongCapacityMutableElementsAllocator<

                INTERFACE_MUTABLE extends IMutableElements,
                CLASS_MUTABLE extends BaseADTElements<?, ?, ?> & ICapacity,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends LongCapacityMutableInstanceAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, ELEMENTS, MUTABLE_FROM>
        implements IMutableElementsAllocator<INTERFACE_MUTABLE, MUTABLE_FROM> {

    protected LongCapacityMutableElementsAllocator(LongFunction<ELEMENTS> createElements) {
        super(createElements, ICapacity::getCapacity);
    }
}
