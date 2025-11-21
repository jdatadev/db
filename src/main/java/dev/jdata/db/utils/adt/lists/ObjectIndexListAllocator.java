package dev.jdata.db.utils.adt.lists;

import java.util.Comparator;
import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElementsAllocators;
import dev.jdata.db.utils.adt.elements.IObjectIterableOnlyElementsView;
import dev.jdata.db.utils.adt.elements.OnlyElementsAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

abstract class ObjectIndexListAllocator<

            T,
            IMMUTABLE extends IIndexList<T>,
            HEAP_IMMUTABLE extends IIndexList<T> & IHeapContainsMarker,
            INTERFACE_MUTABLE extends IMutableIndexList<T>,
            CLASS_MUTABLE extends MutableObjectIndexList<T>,
            BUILDER extends IIndexListBuilder<T, IMMUTABLE, HEAP_IMMUTABLE>>

    extends ListAllocator<IMMUTABLE, HEAP_IMMUTABLE, T[], INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER>
    implements IIndexListAllocator<T, IMMUTABLE, INTERFACE_MUTABLE, BUILDER> {

    ObjectIndexListAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, T[]> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }

    @Override
    public final IMMUTABLE sortedOf(IObjectIterableOnlyElementsView<T> elements, Comparator<? super T> comparator) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);

        return OnlyElementsAllocator.sortedOf(elements, comparator, this);
    }

    @Override
    protected final long getElementsArrayLength(T[] elementsArray) {

        Objects.requireNonNull(elementsArray);

        return elementsArray.length;
    }
}
