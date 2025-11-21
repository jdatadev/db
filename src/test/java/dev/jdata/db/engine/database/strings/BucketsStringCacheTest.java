package dev.jdata.db.engine.database.strings;

import dev.jdata.db.engine.database.strings.BucketsStringCache;

public final class BucketsStringCacheTest extends BaseStringCacheTest<BucketsStringCache> {

    @Override
    BucketsStringCache createStringCache(int initialOuterCapacityExponent, int innerCapacityExponent) {

        return new BucketsStringCache(initialOuterCapacityExponent, innerCapacityExponent);
    }
}
