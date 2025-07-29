package dev.jdata.db.utils.adt.maps;

public final class MutableIntToObjectWithRemoveNonBucketMapTest

        extends BaseMutableIntToObjectNonContainsKeyNonBucketMapTest<Integer, MutableIntToObjectWithRemoveNonBucketMap<Integer>> {

    @Override
    int objectToInt(Integer object) {

        return object.intValue();
    }

    @Override
    Integer intToObject(int integer) {

        return Integer.valueOf(integer);
    }

    @Override
    MutableIntToObjectWithRemoveNonBucketMap<Integer> createMap(int initialCapacityExponent) {

        return new MutableIntToObjectWithRemoveNonBucketMap<>(initialCapacityExponent, Integer[]::new);
    }

    @Override
    Integer[] createValuesArray(int length) {

        return new Integer[length];
    }

    @Override
    boolean remove(MutableIntToObjectWithRemoveNonBucketMap<Integer> map, int key) {

        map.remove(key);

        return true;
    }
}
