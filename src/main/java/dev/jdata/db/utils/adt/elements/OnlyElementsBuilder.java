package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

@Deprecated // what about arrays?
public abstract class OnlyElementsBuilder<

                T extends IElements & IOnlyElementsView,
                U extends IElements & IOnlyElementsView & IHeapContainsMarker,
                V extends IMutableElements & IOnlyElementsMutable>

        extends ElementsBuilder<T, U, V> {

    protected <P> OnlyElementsBuilder(AllocationType allocationType, long minimumCapacity, P parameter, IBuilderMutableAllocator<V, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    private long getCapacity() {

        return getMutable().getCapacity();
    }

    private int getIntCapacity() {

        return BaseADTElements.intCapacity(getCapacity());
    }
}
