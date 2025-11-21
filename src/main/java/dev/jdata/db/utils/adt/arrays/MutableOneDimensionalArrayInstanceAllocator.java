package dev.jdata.db.utils.adt.arrays;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.elements.IntCapacityMutableElementsAllocator;

abstract class MutableOneDimensionalArrayInstanceAllocator<

                INTERFACE_MUTABLE extends IMutableOneDimensionalArray,
                CLASS_MUTABLE extends BaseOneDimensionalArray<?> & IMutableOneDimensionalArray,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends IntCapacityMutableElementsAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, ELEMENTS, MUTABLE_FROM>
        implements IMutableOneDimensionalArrayInstanceAllocator<INTERFACE_MUTABLE, MUTABLE_FROM> {

    MutableOneDimensionalArrayInstanceAllocator(IntFunction<ELEMENTS> createElements) {
        super(createElements);
    }
}
