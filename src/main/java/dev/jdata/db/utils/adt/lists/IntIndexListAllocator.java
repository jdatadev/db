package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.adt.elements.IIntIterableOnlyElementsView;
import dev.jdata.db.utils.adt.elements.OnlyElementsAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.function.IntComparator;

abstract class IntIndexListAllocator<

                IMMUTABLE extends IIntIndexList,
                HEAP_IMMUTABLE extends IIntIndexList & IHeapContainsMarker,
                INTERFACE_MUTABLE extends IMutableIntIndexList,
                CLASS_MUTABLE extends MutableIntIndexList,
                BUILDER extends IIntIndexListBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends ListAllocator<IMMUTABLE, HEAP_IMMUTABLE, int[], INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER>
        implements IIntIndexListAllocator<IMMUTABLE, INTERFACE_MUTABLE, BUILDER> {

    IntIndexListAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, int[]> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }

    @Override
    public final IMMUTABLE sortedOf(IIntIterableOnlyElementsView elements, IntComparator comparator) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);

        return OnlyElementsAllocator.sortedOf(elements, comparator, this);
    }

    @Override
    protected final long getElementsArrayLength(int[] elementsArray) {

        Objects.requireNonNull(elementsArray);

        return elementsArray.length;
    }
}
