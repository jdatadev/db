package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

abstract class MutableLongToObjectMaxDistanceNonBucketMapAllocator<V, T extends IMutableLongToObjectDynamicMap<V>, U extends MutableLongToObjectMaxDistanceNonBucketMap<V>>

        extends BaseMutableMapAllocator<T, U, V[]>
        implements IMutableLongToObjectDynamicMapAllocator<V, T> {

    MutableLongToObjectMaxDistanceNonBucketMapAllocator(IntFunction<V[]> createElements) {
        super(createElements);
    }
}
