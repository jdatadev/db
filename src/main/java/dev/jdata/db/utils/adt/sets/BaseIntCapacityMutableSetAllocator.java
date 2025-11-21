package dev.jdata.db.utils.adt.sets;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.elements.IntCapacityMutableElementsAllocator;

abstract class BaseIntCapacityMutableSetAllocator<

                INTERFACE_MUTABLE extends IMutableElements & IMutableSetType,
                CLASS_MUTABLE extends BaseIntCapacityExponentSet<?, ?> & IMutableSetType,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends IntCapacityMutableElementsAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, ELEMENTS, MUTABLE_FROM> {

    BaseIntCapacityMutableSetAllocator(IntFunction<ELEMENTS> createSetArray) {
        super(createSetArray);
    }
}
