package dev.jdata.db.utils.adt.maps;

import java.util.List;

public final class IntToObjectMapTest extends BaseIntToIntegerOrObjectTest<String[], IntToObjectMap<String>> {

    @Override
    IntToObjectMap<String> createMap(int initialCapacityExponent) {

        return new IntToObjectMap<>(initialCapacityExponent, String[]::new);
    }

    @Override
    String[] createValuesArray(int length) {

        return new String[length];
    }

    @Override
    int getValue(String[] values, int index) {

        return valueToInt(values[index]);
    }

    @Override
    <T> void forEachKeysAndValues(IntToObjectMap<String> map, T parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <T> void forEachKeysAndValues(IntToObjectMap<String> map, T parameter, List<Integer> keysDst, List<Integer> valuesDst, List<T> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(k);
            valuesDst.add(valueToInt(v));
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(IntToObjectMap<String> map, int[] keysDst, String[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int get(IntToObjectMap<String> map, int key) {

        return valueToInt(map.get(key));
    }

    @Override
    void put(IntToObjectMap<String> map, int key, int value) {

        map.put(key, String.valueOf(value));
    }

    private static int valueToInt(String value) {

        return Integer.parseInt(value);
    }
}
