package dev.jdata.db.utils.adt.maps;

public final class IntToObjectMapTest extends BaseIntToIntegerOrObjectTest<String[], IntToObjectMap<String>> {

    @Override
    IntToObjectMap<String> createMap(int initialCapacity) {

        return new IntToObjectMap<>(initialCapacity, String[]::new);
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
