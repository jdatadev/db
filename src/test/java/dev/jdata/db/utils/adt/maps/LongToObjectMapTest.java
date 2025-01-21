package dev.jdata.db.utils.adt.maps;

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
