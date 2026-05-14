package dev.jdata.db.utils.adt.maps;

abstract class MutableLongToObjectDynamicMapAllocator<V, T extends IMutableLongToObjectDynamicMap<V>, U extends MutableLongToObjectMaxDistanceNonBucketMap<V, U>>

        extends BaseMutableLongToObjectDynamicMapAllocator<V, T, U>
        implements IMutableLongToObjectDynamicMapAllocator<V, T> {

}
