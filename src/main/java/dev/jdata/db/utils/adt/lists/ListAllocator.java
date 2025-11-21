package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.adt.elements.IElementsIterable;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IOnlyElementsBuilder;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;
import dev.jdata.db.utils.adt.elements.IOrderedElementsMutable;
import dev.jdata.db.utils.adt.elements.SortableOnlyElementsAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

abstract class ListAllocator<

                IMMUTABLE extends IElements & IListType,
                HEAP_IMMUTABLE extends IElements & IListType & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                INTERFACE_MUTABLE extends IMutableElements & IMutableListType,
                CLASS_MUTABLE
                        extends BaseADTElements<ELEMENTS_ARRAY, ELEMENTS_ARRAY, ELEMENTS_ARRAY>
                                & IMutableElements
                                & IListTypeMutable
                                & IOrderedAddable<SORT_ITERABLE_ELEMENTS>
                                & IOrderedElementsMutable<SORT_COMPARATOR>
                                & IMutableListType,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                SORT_ITERABLE_ELEMENTS extends IElementsIterable,
                SORT_COMPARATOR>

        extends SortableOnlyElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER, SORT_ITERABLE_ELEMENTS, SORT_COMPARATOR> {

    ListAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, ELEMENTS_ARRAY> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }
}
