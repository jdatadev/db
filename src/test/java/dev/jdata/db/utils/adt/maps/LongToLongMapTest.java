package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.scalars.Integers;

public final class LongToLongMapTest extends BaseLongToIntegerOrObjectTest<long[], LongToLongMap> {

    @Override
    LongToLongMap createMap(int initialCapacityExponent) {

        return new LongToLongMap(initialCapacityExponent);
    }

    @Override
    long[] createValuesArray(int length) {

        return new long[length];
    }

    @Override
    int getValue(long[] values, int index) {

        return Integers.checkUnsignedLongToUnsignedInt(values[index]);
    }

    @Override
    <T> void forEachKeysAndValues(LongToLongMap map, T parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <T> void forEachKeysAndValues(LongToLongMap map, T parameter, List<Integer> keysDst, List<Integer> valuesDst, List<T> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(Integers.checkUnsignedLongToUnsignedInt(k));
            valuesDst.add(Integers.checkUnsignedLongToUnsignedInt(v));
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(LongToLongMap map, long[] keysDst, long[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int get(LongToLongMap map, int key) {

        return Integers.checkUnsignedLongToUnsignedInt(map.get(key));
    }

    @Override
    void put(LongToLongMap map, int key, int value) {

        map.put(key, value);
    }
}
