package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public abstract class SortableOnlyElementsAllocator<

                IMMUTABLE extends IElements & IOnlyElementsView,
                HEAP_IMMUTABLE extends IElements & IOnlyElementsView & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                INTERFACE_MUTABLE extends IMutableElements & IOnlyElementsMutable,
                CLASS_MUTABLE
                        extends BaseADTElements<?, ELEMENTS_ARRAY, ?>
                                & IMutableElements
                                & IOnlyElementsMutable
                                & IOrderedAddable<SORT_ITERABLE_ELEMENTS>
                                & IOrderedElementsMutators<SORT_COMPARATOR  >,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>,
                SORT_ITERABLE_ELEMENTS extends IElementsIterable,
                SORT_COMPARATOR>

        extends OnlyElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER>
        implements ISortableAllocator<SORT_ITERABLE_ELEMENTS, SORT_COMPARATOR, IMMUTABLE> {

    protected final void checkSortedOfParameters(SORT_ITERABLE_ELEMENTS elements, SORT_COMPARATOR comparator) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);
    }

    protected static <
                    ITERABLE extends IElementsIterable,
                    COMPARATOR,
                    IMMUTABLE extends IElements & IOnlyElementsView,
                    ELEMENTS_ARRAY,
                    MUTABLE extends BaseADTElements<?, ELEMENTS_ARRAY, ?>
                                    & IMutableElements
                                    & IOnlyElementsMutable
                                    & IOrderedAddable<ITERABLE>
                                    & IOrderedElementsMutators<COMPARATOR>,
                    ALLOCATOR extends SortableOnlyElementsAllocator<IMMUTABLE, ?, ELEMENTS_ARRAY, ?, MUTABLE, ?, ITERABLE, COMPARATOR>>

    IMMUTABLE sortedOf(ITERABLE elements, COMPARATOR comparator, ALLOCATOR allocator) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(allocator);

        return SortHelper.<ITERABLE, COMPARATOR, IMMUTABLE, MUTABLE, ALLOCATOR>sortedOf(elements, comparator, allocator, (a, n) -> a.allocateMutable(n),
                (m, a) -> a.freeMutable(m), (m, a) -> a.makeFromClassMutableToImmutableAndRecreateOrDispose(m), a -> a.emptyImmutable());
    }

    protected SortableOnlyElementsAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, ELEMENTS_ARRAY> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }
}
