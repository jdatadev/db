package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

@Deprecated // remove implements interface for this and similar
abstract class BaseLongToObjectDynamicMapAllocator<

                VALUE,
                IMMUTABLE extends IBaseLongToObjectDynamicMap<VALUE>,
                HEAP_IMMUTABLE extends IBaseLongToObjectDynamicMap<VALUE> & IHeapContainsMarker,
                INTERFACE_MUTABLE extends IBaseMutableLongToObjectDynamicMap<VALUE>,
                CLASS_MUTABLE extends BaseADTElements<Void, CLASS_MUTABLE, Void> & IBaseMutableLongToObjectDynamicMap<VALUE>,
                BUILDER extends IBaseLongToObjectDynamicMapBuilder<VALUE, IMMUTABLE, HEAP_IMMUTABLE>>

        extends MapAllocator<IMMUTABLE, HEAP_IMMUTABLE, INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER>
        implements IBaseLongToObjectDynamicMapAllocator<VALUE, IMMUTABLE, INTERFACE_MUTABLE, BUILDER> {

    BaseLongToObjectDynamicMapAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, Void> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }
}

