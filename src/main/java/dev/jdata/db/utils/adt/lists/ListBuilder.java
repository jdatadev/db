package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IntCapacityOnlyElementsBuilder;

abstract class ListBuilder<

                IMMUTABLE extends IElements & IListType,
                HEAP_IMMUTABLE extends IElements & IListType & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                MUTABLE extends BaseADTElements<ELEMENTS_ARRAY, ELEMENTS_ARRAY, ELEMENTS_ARRAY> & IMutableElements & IListTypeMutable>

        extends IntCapacityOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, MUTABLE> {

    <P> ListBuilder(AllocationType allocationType, long minimumCapacity, P parameter, IIntCapacityBuilderMutableAllocator<MUTABLE, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }
}
