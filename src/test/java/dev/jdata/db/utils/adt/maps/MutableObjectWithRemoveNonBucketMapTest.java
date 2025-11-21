package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.sets.IMutableSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableObjectWithRemoveNonBucketMapTest

        extends BaseMutableObjectToObjectNonContainsKeyNonBucketMapTest<String, StringBuilder, MutableObjectWithRemoveNonBucketMap<String, StringBuilder>> {

    @Override
    boolean supportsContainsKey() {

        return false;
    }

    @Override
    MutableObjectWithRemoveNonBucketMap<String, StringBuilder> createMap(int initialCapacityExponent) {

        return new HeapMutableObjectWithRemoveNonBucketMap<>(AllocationType.HEAP, initialCapacityExponent, String[]::new, StringBuilder[]::new);
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
    IMutableSet<String> createKeysAddable(int initialCapacity) {

        return createObjectAddable(initialCapacity, String[]::new);
    }

    @Override
    IMutableSet<StringBuilder> createValuesAddable(int initialCapacity) {

        return createObjectAddable(initialCapacity, StringBuilder[]::new);
    }

    @Override
    String[] keysToArray(IMutableSet<String> keysAddable) {

        return toArray(keysAddable, String[]::new);
    }

    @Override
    StringBuilder[] valuesToArray(IMutableSet<StringBuilder> valuesAddable) {

        return toArray(valuesAddable, StringBuilder[]::new);
    }

    @Override
    boolean remove(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, int key) {

        map.remove(stringKey(key));

        return true;
    }

    @Override
    int keyToInteger(String object) {

        return intKey(object);
    }

    @Override
    String integerToKey(int integer) {

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
