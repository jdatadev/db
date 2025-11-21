package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

abstract class LongToIntStaticMapBuilder<T extends ILongToIntStaticMap, U extends ILongToIntStaticMap & IHeapContainsMarker, V extends MutableLongToIntNonRemoveNonBucketMap<V>>

        extends MapBuilder<T, U, V>
        implements ILongToIntStaticMapBuilder<T, U> {

    <P> LongToIntStaticMapBuilder(AllocationType allocationType, int minimumCapacity, P parameter, IIntCapacityBuilderMutableAllocator<V, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }
}
