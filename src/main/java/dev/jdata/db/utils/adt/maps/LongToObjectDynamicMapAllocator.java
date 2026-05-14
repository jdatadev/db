package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

abstract class LongToObjectDynamicMapAllocator<

                VALUE,
                IMMUTABLE extends ILongToObjectDynamicMap<VALUE>,
                HEAP_IMMUTABLE extends ILongToObjectDynamicMap<VALUE> & IHeapContainsMarker,
                INTERFACE_MUTABLE extends IMutableLongToObjectDynamicMap<VALUE>,
                CLASS_MUTABLE extends BaseADTElements<Void, CLASS_MUTABLE, Void> & IMutableLongToObjectDynamicMap<VALUE>,
                BUILDER extends ILongToObjectDynamicMapBuilder<VALUE, IMMUTABLE, HEAP_IMMUTABLE>>

        extends BaseLongToObjectDynamicMapAllocator<VALUE, IMMUTABLE, HEAP_IMMUTABLE, INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER>
        implements ILongToObjectDynamicMapAllocator<VALUE, IMMUTABLE, INTERFACE_MUTABLE, BUILDER> {

    LongToObjectDynamicMapAllocator(AllocationType allocationType,
            IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, Void> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }
}
