package dev.jdata.db.utils.adt.arrays;

import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.elements.LongCapacityMutableElementsAllocator;

abstract class MutableOneDimensionalLargeArrayInstanceAllocator<

                INTERFACE_MUTABLE extends IMutableOneDimensionalArray,
                CLASS_MUTABLE extends BaseOneDimensionalLargeArray<?, ?> & IMutableOneDimensionalArray,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends LongCapacityMutableElementsAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, ELEMENTS, MUTABLE_FROM>
        implements IMutableOneDimensionalLargeArrayInstanceAllocator<INTERFACE_MUTABLE, MUTABLE_FROM> {

    MutableOneDimensionalLargeArrayInstanceAllocator(LongFunction<ELEMENTS> createElements) {
        super(createElements);
    }
}
