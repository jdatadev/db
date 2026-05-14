package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public abstract class OnlyElementsAllocator<

                IMMUTABLE extends IElements & IOnlyElementsView,
                HEAP_IMMUTABLE extends IElements & IOnlyElementsView & IHeapContainsMarker,
                ALLOCATE_FROM_ARRAY,
                INTERFACE_MUTABLE extends IMutableElements & IOnlyElementsMutable,
                CLASS_MUTABLE extends BaseADTElements<?, ?, ?> & IMutableElements & IOnlyElementsMutable,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends ElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, ALLOCATE_FROM_ARRAY, INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER> {

    protected OnlyElementsAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, ALLOCATE_FROM_ARRAY> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }
}
