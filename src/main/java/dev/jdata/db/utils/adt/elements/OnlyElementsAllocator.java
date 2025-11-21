package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public abstract class OnlyElementsAllocator<

                IMMUTABLE extends IElements & IOnlyElementsView,
                HEAP_IMMUTABLE extends IElements & IOnlyElementsView & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                INTERFACE_MUTABLE extends IMutableElements & IOnlyElementsMutable,
                CLASS_MUTABLE extends BaseADTElements<?, ELEMENTS_ARRAY, ?> & IMutableElements & IOnlyElementsMutable,
                BUILDER extends IOnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends ElementsAllocator<IMMUTABLE, HEAP_IMMUTABLE, ELEMENTS_ARRAY, INTERFACE_MUTABLE, CLASS_MUTABLE, BUILDER> {

    protected static <
                    ITERABLE extends IElementsIterable & IOnlyElementsView,
                    COMPARATOR,
                    IMMUTABLE extends IElements & IOnlyElementsView,
                    ELEMENTS_ARRAY,
                    MUTABLE extends BaseADTElements<?, ELEMENTS_ARRAY, ?>
                                    & IMutableElements
                                    & IOnlyElementsMutable
                                    & IOrderedAddable<ITERABLE>
                                    & IOrderedElementsMutators<COMPARATOR>,
                    ALLOCATOR extends OnlyElementsAllocator<IMMUTABLE, ?, ELEMENTS_ARRAY, ?, MUTABLE, ?>>

    IMMUTABLE sortedOf(ITERABLE elements, COMPARATOR comparator, ALLOCATOR allocator) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);

        return SortHelper.<ITERABLE, COMPARATOR, IMMUTABLE, MUTABLE, ALLOCATOR>sortedOf(elements, comparator, allocator, (a, n) -> a.allocateMutable(n),
                (m, a) -> a.freeMutable(m), (m, a) -> a.makeFromClassMutableToImmutableAndRecreateOrDispose(m), a -> a.emptyImmutable());
    }

    protected OnlyElementsAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, ELEMENTS_ARRAY> elementsAllocators) {
        super(allocationType, elementsAllocators);
    }
}
