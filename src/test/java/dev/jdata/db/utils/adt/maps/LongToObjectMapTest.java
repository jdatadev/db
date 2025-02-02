package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.scalars.Integers;

public final class LongToObjectMapTest extends BaseLongToIntegerOrObjectTest<String[], LongToObjectMap<String>> {

    @Override
    LongToObjectMap<String> createMap(int initialCapacity) {

        return new LongToObjectMap<>(initialCapacity, String[]::new);
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
    <T> void forEachKeysAndValues(LongToObjectMap<String> map, T parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <T> void forEachKeysAndValues(LongToObjectMap<String> map, T parameter, List<Integer> keysDst, List<Integer> valuesDst, List<T> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(Integers.checkUnsignedLongToUnsignedInt(k));
            valuesDst.add(valueToInt(v));
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(LongToObjectMap<String> map, long[] keysDst, String[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int get(LongToObjectMap<String> map, int key) {

        return valueToInt(map.get(key));
    }

    @Override
    void put(LongToObjectMap<String> map, int key, int value) {

        map.put(key, String.valueOf(value));
    }

    private static int valueToInt(String value) {

        return Integer.parseInt(value);
    }
}
