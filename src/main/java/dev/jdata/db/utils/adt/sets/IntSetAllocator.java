package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

abstract class IntSetAllocator<

                IMMUTABLE extends IIntSet,
                HEAP_IMMUTABLE extends IIntSet & IHeapContainsMarker,
                INTERFACE_MUTABLE extends IMutableIntSet,
                CLASS_MUTABLE extends BaseADTElements<int[], int[], int[]> & IMutableIntSet,
                BUILDER extends IIntSetBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends BaseIntSetBuilderAllocator<IMMUTABLE, HEAP_IMMUTABLE, INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER>
        implements IIntSetAllocator<IMMUTABLE, INTERFACE_MUTABLE, BUILDER> {

    IntSetAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, int[]> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }

    @Override
    protected final long getElementsArrayLength(int[] elementsArray) {

        Objects.requireNonNull(elementsArray);

        return elementsArray.length;
    }
}
