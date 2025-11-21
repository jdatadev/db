package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IntCapacityMutableElementsAllocator;

abstract class BaseMutableListAllocator<T extends IMutableElements & IMutableListType, U extends BaseADTElements<?, ?, ?> & IMutableListType, V>

        extends IntCapacityMutableElementsAllocator<T, U, V> {

    BaseMutableListAllocator(IntFunction<V> createElements) {
        super(createElements);
    }
}
