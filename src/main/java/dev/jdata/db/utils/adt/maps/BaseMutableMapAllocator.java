package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IntCapacityMutableElementsAllocator;

abstract class BaseMutableMapAllocator<T extends IMutableBaseMap<?>, U extends BaseADTElements<?, ?, ?> & IMutableMapType, V>

        extends IntCapacityMutableElementsAllocator<T, U, V> {

    BaseMutableMapAllocator(IntFunction<V> createMapArray) {
        super(createMapArray);
    }
}
