package dev.jdata.db.utils.adt.maps;

abstract class MutableLongToIntNonRemoveStaticMapAllocator<T extends IMutableLongToIntNonRemoveStaticMap, U extends MutableLongToIntNonRemoveNonBucketMap<U>>

        extends BaseMutableMapAllocator<T, U, ILongToIntMapView>
        implements IMutableLongToIntNonRemoveStaticMapAllocator<T> {

}
