package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IntCapacityOnlyElementsBuilder;

abstract class SetBuilder<

                IMMUTABLE extends IElements & ISetType,
                HEAP_IMMUTABLE extends IElements & ISetType & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                MUTABLE extends BaseADTElements<?, ELEMENTS_ARRAY, ?> & IMutableElements & IMutableSetType>

        extends IntCapacityOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, MUTABLE> {

    <P> SetBuilder(AllocationType allocationType, int minimumCapacity, P parameter, IIntCapacityBuilderMutableAllocator<MUTABLE, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }
}
