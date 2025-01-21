package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.scalars.Integers;

public final class LongToLongMapTest extends BaseLongToIntegerOrObjectTest<long[], LongToLongMap> {

    @Override
    LongToLongMap createMap(int initialCapacity) {

        return new LongToLongMap(initialCapacity);
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
