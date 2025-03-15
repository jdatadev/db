package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.scalars.Integers;

public final class LongToIntMapTest extends BaseLongToIntegerOrObjectTest<int[], LongToIntMap> {

    @Override
    LongToIntMap createMap(int initialCapacityExponent) {

        return new LongToIntMap(initialCapacityExponent);
    }

    @Override
    int[] createValuesArray(int length) {

        return new int[length];
    }

    @Override
    int getValue(int[] values, int index) {

        return Integers.checkUnsignedLongToUnsignedInt(values[index]);
    }

    @Override
    <T> void forEachKeysAndValues(LongToIntMap map, T parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <T> void forEachKeysAndValues(LongToIntMap map, T parameter, List<Integer> keysDst, List<Integer> valuesDst, List<T> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(Integers.checkUnsignedLongToUnsignedInt(k));
            valuesDst.add(Integers.checkUnsignedLongToUnsignedInt(v));
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(LongToIntMap map, long[] keysDst, int[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int get(LongToIntMap map, int key) {

        return Integers.checkUnsignedLongToUnsignedInt(map.get(key));
    }

    @Override
    void put(LongToIntMap map, int key, int value) {

        map.put(key, value);
    }
}
