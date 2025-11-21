package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.elements.IntCapacityMutableElementsAllocator;

abstract class BaseMutableListAllocator<

                INTERFACE_MUTABLE extends IMutableElements & IMutableListType,
                CLASS_MUTABLE extends BaseADTElements<?, ?, ?> & IMutableListType,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends IntCapacityMutableElementsAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, ELEMENTS, MUTABLE_FROM> {

    BaseMutableListAllocator(IntFunction<ELEMENTS> createElements) {
        super(createElements);
    }
}
