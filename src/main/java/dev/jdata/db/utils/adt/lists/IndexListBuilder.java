package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.checks.Checks;

abstract class IndexListBuilder<

            T,
            IMMUTABLE extends IIndexList<T>,
            MUTABLE extends MutableObjectIndexList<T, IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>,
            HEAP_IMMUTABLE extends IIndexList<T> & IHeapContainsMarker,
            BUILDER extends IIndexListBuilder<T, IMMUTABLE, HEAP_IMMUTABLE>,
            BUILDER_ALLOCATOR extends IndexListAllocator<T, IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, BUILDER, BUILDER_ALLOCATOR>>

            extends ListBuilder<IMMUTABLE, MUTABLE, HEAP_IMMUTABLE, T[], BUILDER, BUILDER_ALLOCATOR>
            implements IIndexListBuilder<T, IMMUTABLE, HEAP_IMMUTABLE> {

    IndexListBuilder(AllocationType allocationType, int minimumCapacity, BUILDER_ALLOCATOR listAllocator) {
        super(allocationType, minimumCapacity, listAllocator);
    }

    @Override
    public final void addTail(T instance) {

        checkIsAllocated();

        getMutable().addTailElement(instance);
    }

    @Override
    public final void addTail(@SuppressWarnings("unchecked") T ... instances) {

        Checks.isNotEmpty(instances);

        checkIsAllocated();

        getMutable().addTailElements(instances);
    }

    @Override
    public final void addTail(IObjectIterableElementsView<T> elements) {

        Objects.requireNonNull(elements);

        checkIsAllocated();

        getMutable().addTail(elements);
    }

    final long getListCapacity() {

        return getCapacity();
    }
}
