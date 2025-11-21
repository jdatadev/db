package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.checks.Checks;

abstract class ObjectIndexListBuilder<

                T,
                IMMUTABLE extends IIndexList<T>,
                HEAP_IMMUTABLE extends IIndexList<T> & IHeapContainsMarker,
                MUTABLE extends MutableObjectIndexList<T>,
                BUILDER extends IIndexListBuilder<T, IMMUTABLE, HEAP_IMMUTABLE>>

        extends ListBuilder<IMMUTABLE, HEAP_IMMUTABLE, T[], MUTABLE>
        implements IIndexListBuilder<T, IMMUTABLE, HEAP_IMMUTABLE> {

    <P> ObjectIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter, IIntCapacityBuilderMutableAllocator<MUTABLE, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    public final void addTail(T instance) {

        Objects.requireNonNull(instance);

        getMutable().addTailElement(instance);
    }

    @Override
    public final void addTail(@SuppressWarnings("unchecked") T ... instances) {

        Checks.isNotEmpty(instances);

        getMutable().addTailElements(instances);
    }

    @Override
    public final void addTail(IObjectIterableElementsView<T> elements) {

        Checks.isNotEmpty(elements);

        getMutable().addTail(elements);
    }
}
