package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;

abstract class IntSetBuilder<T extends IIntSet, U extends IIntSet & IHeapContainsMarker, V extends BaseADTElements<int[], int[], int[]> & IMutableIntSet>

        extends SetBuilder<T, U, int[], V>
        implements IIntSetBuilder<T, U> {

    <P> IntSetBuilder(AllocationType allocationType, int minimumCapacity, P parameter, IIntCapacityBuilderMutableAllocator<V, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    public final void addUnordered(int value) {

        getMutable().addUnordered(value);
    }
}