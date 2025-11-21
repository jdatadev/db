package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IntCapacityOnlyElementsBuilder;

abstract class MapBuilder<

                T extends IElements & IMapType,
                U extends IElements & IMapType & IHeapContainsMarker,
                V extends BaseADTElements<Void, V, Void> & IMutableElements & IMapTypeMutable>

        extends IntCapacityOnlyElementsBuilder<T, U, V, V> {

    <P> MapBuilder(AllocationType allocationType, long minimumCapacity, P parameter, IIntCapacityBuilderMutableAllocator<V, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }
}
