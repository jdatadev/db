package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

abstract class MutableIntToObjectWithRemoveNonBucketMapAllocator<V, T extends IMutableIntToObjectWithRemoveStaticMap<V>, U extends MutableIntToObjectWithRemoveNonBucketMap<V>>

        extends BaseMutableMapAllocator<T, U, V[]>
        implements IMutableIntToObjectWithRemoveStaticMapAllocator<V, T> {

    MutableIntToObjectWithRemoveNonBucketMapAllocator(IntFunction<V[]> createElements) {
        super(createElements);
    }
}
