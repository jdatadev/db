package dev.jdata.db.utils.adt.sets;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IntCapacityMutableElementsAllocator;

abstract class BaseIntCapacityMutableSetAllocator<T extends IMutableElements & IMutableSetType, U extends BaseIntCapacityExponentSet<?, ?> & IMutableSetType, V>

        extends IntCapacityMutableElementsAllocator<T, U, V> {

    BaseIntCapacityMutableSetAllocator(IntFunction<V> createSetArray) {
        super(createSetArray);
    }
}
