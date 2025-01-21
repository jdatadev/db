package dev.jdata.db.utils.adt.maps;

public final class IntToIntMapTest extends BaseIntToIntegerOrObjectTest<int[], IntToIntMap> {

    @Override
    IntToIntMap createMap(int initialCapacity) {

        return new IntToIntMap(initialCapacity);
    }

    @Override
    int[] createValuesArray(int length) {

        return new int[length];
    }

    @Override
    int getValue(int[] values, int index) {

        return values[index];
    }

    @Override
    void keysAndValues(IntToIntMap map, int[] keysDst, int[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int get(IntToIntMap map, int key) {

        return map.get(key);
    }

    @Override
    void put(IntToIntMap map, int key, int value) {

        map.put(key, value);
    }
}
