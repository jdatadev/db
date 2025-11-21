package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.adt.elements.ILongIterableOnlyElementsView;
import dev.jdata.db.utils.adt.elements.OnlyElementsAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.function.LongComparator;

abstract class LongIndexListAllocator<

                IMMUTABLE extends ILongIndexList,
                HEAP_IMMUTABLE extends ILongIndexList & IHeapContainsMarker,
                INTERFACE_MUTABLE extends IMutableLongIndexList,
                CLASS_MUTABLE extends MutableLongIndexList,
                BUILDER extends ILongIndexListBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends ListAllocator<IMMUTABLE, HEAP_IMMUTABLE, long[], INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER>
        implements ILongIndexListAllocator<IMMUTABLE, INTERFACE_MUTABLE, BUILDER> {

    LongIndexListAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, long[]> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }

    @Override
    public final IMMUTABLE sortedOf(ILongIterableOnlyElementsView elements, LongComparator comparator) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);

        return OnlyElementsAllocator.sortedOf(elements, comparator, this);
    }

    @Override
    protected final long getElementsArrayLength(long[] elementsArray) {

        Objects.requireNonNull(elementsArray);

        return elementsArray.length;
    }
}
