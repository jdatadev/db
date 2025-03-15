package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.scalars.Integers;

public final class IntToLongMapTest extends BaseIntToIntegerOrObjectTest<long[], IntToLongMap> {

    @Override
    IntToLongMap createMap(int initialCapacityExponent) {

        return new IntToLongMap(initialCapacityExponent);
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
    <T> void forEachKeysAndValues(IntToLongMap map, T parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <T> void forEachKeysAndValues(IntToLongMap map, T parameter, List<Integer> keysDst, List<Integer> valuesDst, List<T> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(k);
            valuesDst.add(Integers.checkUnsignedLongToUnsignedInt(v));
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(IntToLongMap map, int[] keysDst, long[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int get(IntToLongMap map, int key) {

        return Integers.checkUnsignedLongToUnsignedInt(map.get(key));
    }

    @Override
    void put(IntToLongMap map, int key, int value) {

        map.put(key, value);
    }
}
