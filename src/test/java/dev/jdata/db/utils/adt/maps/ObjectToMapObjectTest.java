package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.arrays.Array;

public final class ObjectToMapObjectTest extends BaseIntegerToIntegerOrObjectTest<String[], StringBuilder[], ObjectMap<String, StringBuilder>> {

    @Override
    ObjectMap<String, StringBuilder> createMap(int initialCapacityExponent) {

        return new ObjectMap<>(initialCapacityExponent, String[]::new, StringBuilder[]::new);
    }

    @Override
    String[] createKeysArray(int length) {

        return new String[length];
    }

    @Override
    StringBuilder[] createValuesArray(int length) {

        return new StringBuilder[length];
    }

    @Override
    int getKey(String[] keys, int index) {

        return parseInt(keys[index]);
    }

    @Override
    int getValue(StringBuilder[] values, int index) {

        return parseInt(values[index]);
    }

    @Override
    boolean contains(ObjectMap<String, StringBuilder> map, int key) {

        return map.containsKey(stringKey(key));
    }

    @Override
    int get(ObjectMap<String, StringBuilder> map, int key) {

        return parseInt(map.get(stringKey(key)));
    }

    @Override
    int[] getKeys(ObjectMap<String, StringBuilder> map) {

        return Array.mapToInt(map.keys(), k -> parseInt(k));
    }

    @Override
    <T> void forEachKeysAndValues(ObjectMap<String, StringBuilder> map, T parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <T> void forEachKeysAndValues(ObjectMap<String, StringBuilder> map, T parameter, List<Integer> keysDst, List<Integer> valuesDst, List<T> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(parseInt(k));
            valuesDst.add(parseInt(v));
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(ObjectMap<String, StringBuilder> map, String[] keysDst, StringBuilder[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    void put(ObjectMap<String, StringBuilder> map, int key, int value) {

        map.put(stringKey(key), stringBuilderValue(value));
    }

    @Override
    boolean remove(ObjectMap<String, StringBuilder> map, int key) {

        return map.remove(stringKey(key));
    }

    private static String stringKey(int key) {

        return String.valueOf(key);
    }

    private static StringBuilder stringBuilderValue(int value) {

        return new StringBuilder().append(value);
    }

    private static int parseInt(CharSequence charSequence) {

        return Integer.parseInt(charSequence, 0, charSequence.length(), 10);
    }
}
