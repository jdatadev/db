package dev.jdata.db.utils.adt.maps;

import java.util.List;

public final class IntToIntMapTest extends BaseIntToIntegerOrObjectTest<int[], IntToIntMap> {

    @Override
    IntToIntMap createMap(int initialCapacityExponent) {

        return new IntToIntMap(initialCapacityExponent);
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
    <T> void forEachKeysAndValues(IntToIntMap map, T parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <T> void forEachKeysAndValues(IntToIntMap map, T parameter, List<Integer> keysDst, List<Integer> valuesDst, List<T> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(k);
            valuesDst.add(v);
            parameters.add(p);
        });
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
