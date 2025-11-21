package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.elements.IntCapacityMutableElementsAllocator;

abstract class BaseMutableMapAllocator<T extends IMutableBaseMap<?>, U extends BaseADTElements<Void, U, Void> & IMutableMapType, V extends IMutableFrom>

        extends IntCapacityMutableElementsAllocator<T, U, Void, V> {

    BaseMutableMapAllocator() {
        super(c -> null);
    }
}
