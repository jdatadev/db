package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.checks.Checks;

abstract class LongIndexListBuilder<

                IMMUTABLE extends ILongIndexList,
                MUTABLE extends MutableLongIndexList,
                HEAP_IMMUTABLE extends ILongIndexList & IHeapContainsMarker,
                BUILDER extends ILongIndexListBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends ListBuilder<IMMUTABLE, HEAP_IMMUTABLE, long[], MUTABLE>
        implements ILongIndexListBuilder<IMMUTABLE, HEAP_IMMUTABLE> {

    <P> LongIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter, IIntCapacityBuilderMutableAllocator<MUTABLE, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    public final void addTail(long value) {

        getMutable().addTail(value);
    }

    @Override
    public final void addTail(long ... values) {

        Checks.isNotEmpty(values);

        getMutable().addTail(values);
    }

    @Override
    public final void addTail(ILongIterableElementsView elements) {

        Objects.requireNonNull(elements);

        getMutable().addTail(elements);
    }
}
