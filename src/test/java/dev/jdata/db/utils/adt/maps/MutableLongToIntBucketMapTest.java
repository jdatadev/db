package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongToIntBucketMapTest extends BaseMutableLongToIntegerOrObjectBucketMapTest<int[], MutableLongToIntBucketMap> {

    @Override
    MutableLongToIntBucketMap createMap(int initialCapacityExponent) {

        return new MutableLongToIntBucketMap(initialCapacityExponent);
    }

    @Override
    int put(MutableLongToIntBucketMap map, int key, int value, int defaultPreviousValue) {

        return map.put(key, value, defaultPreviousValue);
    }

    @Override
    int removeWithDefaultValue(MutableLongToIntBucketMap map, int key, int defaultValue) {

        return map.removeAndReturnPrevious(key, defaultValue);
    }

    @Override
    int[] createValuesArray(int length) {

        return new int[length];
    }

    @Override
    int getValue(int[] values, int index) {

        return values[index];
    }

    @Override
    int get(MutableLongToIntBucketMap map, int key) {

        throw new UnsupportedOperationException();
    }

    @Override
    int getWithDefaultValue(MutableLongToIntBucketMap map, int key, int defaultValue) {

        return map.get(key, defaultValue);
    }

    @Override
    <P> void forEachKeyAndValueWithNullFunction(MutableLongToIntBucketMap map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeyAndValue(MutableLongToIntBucketMap map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(Integers.checkLongToInt(k));
            valuesDst.add(v);
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableLongToIntBucketMap map, long[] keysDst, int[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }
}
