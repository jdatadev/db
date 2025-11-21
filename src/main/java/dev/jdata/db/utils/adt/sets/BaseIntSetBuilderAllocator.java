package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

abstract class BaseIntSetBuilderAllocator<

                IMMUTABLE extends IBaseIntSet,
                HEAP_IMMUTABLE extends IBaseIntSet & IHeapContainsMarker,
                INTERFACE_MUTABLE extends IBaseMutableIntSet,
                CLASS_MUTABLE extends BaseADTElements<int[], int[], int[]> & IBaseMutableIntSet,
                BUILDER extends IBaseIntSetBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends SetAllocator<IMMUTABLE, HEAP_IMMUTABLE, int[], INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER>
        implements IBaseIntSetAllocator<IMMUTABLE, INTERFACE_MUTABLE, BUILDER> {

    BaseIntSetBuilderAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, int[]> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }
}
