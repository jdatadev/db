package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.checks.Checks;

abstract class LongIndexListBuilder<T extends ILongIndexList, U extends ILongIndexList & IHeapContainsMarker, V extends MutableLongIndexList>

        extends ListBuilder<T, U, long[], V>
        implements ILongIndexListBuilder<T, U> {

    <P> LongIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter, IIntCapacityBuilderMutableAllocator<V, P> builderMutableAllocator) {
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
