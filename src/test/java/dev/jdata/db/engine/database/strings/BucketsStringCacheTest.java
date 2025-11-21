package dev.jdata.db.engine.database.strings;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class BucketsStringCacheTest extends BaseStringCacheTest<BucketsStringCache> {

    @Override
    BucketsStringCache createStringCache(int initialOuterCapacityExponent, int innerCapacityExponent) {

        return new BucketsStringCache(AllocationType.HEAP, initialOuterCapacityExponent, innerCapacityExponent);
    }
}
