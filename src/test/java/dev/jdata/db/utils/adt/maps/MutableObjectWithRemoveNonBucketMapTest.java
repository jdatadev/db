package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableObjectWithRemoveNonBucketMapTest

        extends BaseMutableObjectToObjectNonContainsKeyNonBucketMapTest<String, StringBuilder, MutableObjectWithRemoveNonBucketMap<String, StringBuilder>> {

    @Override
    boolean supportsContainsKey() {

        return false;
    }

    @Override
    MutableObjectWithRemoveNonBucketMap<String, StringBuilder> createMap(int initialCapacityExponent) {

        return new MutableObjectWithRemoveNonBucketMap<>(initialCapacityExponent, String[]::new, StringBuilder[]::new);
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

        return intKey(keys[index]);
    }

    @Override
    int[] getKeys(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map) {

        return Array.closureOrConstantMapToInt(map.keys(), k -> intKey(k));
    }

    @Override
    boolean remove(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, int key) {

        map.remove(stringKey(key));

        return true;
    }

    @Override
    int objectKeyToInt(String object) {

        return intKey(object);
    }

    @Override
    String intKeyToObject(int integer) {

        return stringKey(integer);
    }

    @Override
    int objectValueToInt(StringBuilder object) {

        return intValue(object);
    }

    @Override
    StringBuilder intValueToObject(int integer) {

        return stringBuilderValue(integer);
    }

    private static String stringKey(int key) {

        return String.valueOf(key);
    }

    private static StringBuilder stringBuilderValue(int value) {

        return new StringBuilder().append(value);
    }

    private static int intKey(String key) {

        return parseUnsigedInt(key);
    }

    private static int intValue(StringBuilder value) {

        return parseSignedInt(value);
    }

    private static int parseSignedInt(CharSequence charSequence) {

        return Integers.parseSignedInt(charSequence);
    }

    private static int parseUnsigedInt(CharSequence charSequence) {

        return Integers.parseUnsignedInt(charSequence);
    }
}
