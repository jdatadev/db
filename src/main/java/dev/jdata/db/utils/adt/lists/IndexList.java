package dev.jdata.db.utils.adt.lists;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;

abstract class IndexList<T> extends BaseIndexList<T> implements IBaseIndexList<T> {

    static <
                    T,
                    IMMUTABLE extends IIndexList<T>,
                    MUTABLE extends MutableObjectIndexList<T, IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>,
                    HEAP_IMMUTABLE extends IIndexList<T> & IHeapContainsMarker,
                    BUILDER extends IndexListBuilder<T, IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>,
                    BUILDER_ALLOCATOR extends IndexListAllocator<T, IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>>

    IMMUTABLE sortedOf(IObjectIterableElementsView<T> elements, Comparator<? super T> comparator, BUILDER_ALLOCATOR allocator) {

        Objects.requireNonNull(elements);
        Objects.requireNonNull(comparator);
        Objects.requireNonNull(allocator);

        final IMMUTABLE result;

        final int numElements = IElementsView.intNumElements(elements.getNumElements());

        if (numElements != 0) {

            final MUTABLE sorted = allocator.allocateMutable(numElements);

            sorted.addTail(elements);

            sorted.sort(comparator);

            result = allocator.copyToImmutable(sorted);
        }
        else {
            result = allocator.emptyImmutable();
        }

        return result;
    }
/*
    static <
                    T,
                    IMMUTABLE extends IIndexList<T>,
                    MUTABLE extends MutableObjectIndexList<T, IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>,
                    HEAP_IMMUTABLE extends IIndexList<T> & IHeapContainsMarker,
                    BUILDER extends IndexListBuilder<T, IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>,
                    BUILDER_ALLOCATOR extends IndexListAllocator<T, IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>>

    BUILDER createBuilder(BUILDER_ALLOCATOR listAllocator) {

        return createBuilder(DEFAULT_INITIAL_CAPACITY, listAllocator);
    }

    static <
                    T,
                    IMMUTABLE extends IIndexList<T>,
                    MUTABLE extends MutableObjectIndexList<T, IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>,
                    HEAP_IMMUTABLE extends IIndexList<T> & IHeapContainsMarker,
                    BUILDER extends IndexListBuilder<T, IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>,
                    BUILDER_ALLOCATOR extends IndexListAllocator<T, IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>>

    BUILDER createBuilder(int minimumCapacity, BUILDER_ALLOCATOR listAllocator) {

        Checks.isCapacity(minimumCapacity);
        Objects.requireNonNull(listAllocator);

        return listAllocator.allocateBuilder(minimumCapacity);
    }
*/

    IndexList(AllocationType allocationType) {
        super(allocationType);
    }

    IndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, int initialCapacity) {
        super(allocationType, createElementsArray, initialCapacity);
    }

    IndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T instance) {
        super(allocationType, createElementsArray, instance);
    }

    IndexList(AllocationType allocationType, T[] instances) {
        super(allocationType, instances);
    }

    IndexList(AllocationType allocationType, IntFunction<T[]> createElementsArray, T[] instances, int numElements) {
        super(allocationType, createElementsArray, instances, numElements);
    }

    IndexList(MutableObjectIndexList<T, ?, ?, ?> toCopy) {
        super(toCopy);
    }

    @Override
    public <U extends MutableObjectIndexList<T>> U copyToMutable(IndexListAllocator<T, ? extends IndexList<T>, U, ?, ?, ?> indexListAllocator) {

        Objects.requireNonNull(indexListAllocator);

        final U result = indexListAllocator.allocateMutable(getIntNumElements());

        result.addTail((BaseObjectArrayList<T>)this);

        return result;
    }
}
