package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IOnlyElementsBuilder;
import dev.jdata.db.utils.adt.elements.OnlyElementsAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

abstract class ListAllocator<

                IMMUTABLE extends IElements & IListType,
                HEAP_IMMUTABLE extends IElements & IListType & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                INTERFACE_MUTABLE extends IMutableElements & IMutableListType,
                CLASS_MUTABLE extends BaseADTElements<ELEMENTS_ARRAY, ELEMENTS_ARRAY, ELEMENTS_ARRAY> & IMutableElements & IMutableListType,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends OnlyElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER> {

    ListAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, ELEMENTS_ARRAY> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }
}
