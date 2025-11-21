package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

final class CachedMutableIntToObjectWithRemoveNonBucketMap<V> extends MutableIntToObjectWithRemoveNonBucketMap<V> implements ICachedMutableIntToObjectWithRemoveStaticMap<V> {

    CachedMutableIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);
    }
}
