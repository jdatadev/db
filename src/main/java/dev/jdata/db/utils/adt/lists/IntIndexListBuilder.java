package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;

abstract class IntIndexListBuilder<T extends IIntIndexList, U extends IIntIndexList & IHeapContainsMarker, V extends MutableIntIndexList>

        extends ListBuilder<T, U, int[], V>
        implements IBaseIntIndexListBuilder<T, U> {

    <P> IntIndexListBuilder(AllocationType allocationType, int minimumCapacity, P parameter, IIntCapacityBuilderMutableAllocator<V, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    public final void addTail(int value) {

        getMutable().addTail(value);
    }

    @Override
    public final void addTail(int ... values) {

        getMutable().addTail(values);
    }

    @Override
    public final void addTail(IIntIterableElementsView elements) {

        getMutable().addTail(elements);
    }
}
