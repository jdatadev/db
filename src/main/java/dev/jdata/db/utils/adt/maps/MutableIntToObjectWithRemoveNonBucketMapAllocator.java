package dev.jdata.db.utils.adt.maps;

abstract class MutableIntToObjectWithRemoveNonBucketMapAllocator<

                V,
                T extends IMutableIntToObjectWithRemoveStaticMap<V>,
                U extends MutableIntToObjectWithRemoveNonBucketMap<V, U>>

        extends BaseMutableMapAllocator<T, U, IIntToObjectMapView<V>>
        implements IMutableIntToObjectWithRemoveStaticMapAllocator<V, T> {

}
