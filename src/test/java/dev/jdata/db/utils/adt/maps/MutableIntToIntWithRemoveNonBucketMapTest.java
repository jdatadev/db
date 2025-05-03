package dev.jdata.db.utils.adt.maps;

public final class MutableIntToIntWithRemoveNonBucketMapTest extends BaseMutableIntToIntNonContainsKeyNonBucketMapTest<MutableIntToIntWithRemoveNonBucketMap> {

    @Override
    MutableIntToIntWithRemoveNonBucketMap createMap(int initialCapacityExponent) {

        return new MutableIntToIntWithRemoveNonBucketMap(initialCapacityExponent);
    }

    @Override
    boolean remove(MutableIntToIntWithRemoveNonBucketMap map, int key) {

        map.remove(key);

        return true;
    }
}
