package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseMutableIntToLongNonContainsKeyNonBucketMapTest<

                M extends IIntToLongMap & IClearable & IIntToLongMapMutators & IIntToLongNonContainsNonBucketMapGetters>

        extends BaseMutableIntToIntegerOrObjectTest<long[], M> {

    @Override
    final long[] createValuesArray(int length) {

        return new long[length];
    }

    @Override
    final boolean supportsContainsKey() {

        return false;
    }

    @Override
    final boolean supportsRemoveNonAdded() {

        return false;
    }

    @Override
    final boolean containsKey(M map, int key) {

        throw new UnsupportedOperationException();
    }

    @Override
    final int getValue(long[] values, int index) {

        return Integers.checkLongToInt(values[index]);
    }

    @Override
    final int getWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }

    @Override
    final <P> void forEachKeysAndValues(M map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    final <P> void forEachKeysAndValues(M map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(k);
            valuesDst.add(Integers.checkLongToInt(v));
            parameters.add(p);
        });
    }

    @Override
    final void keysAndValues(M map, int[] keysDst, long[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    final int get(M map, int key) {

        return Integers.checkLongToInt(map.get(key));
    }

    @Override
    final int put(M map, int key, int value, int defaultPreviousValue) {

        return Integers.checkLongToInt(map.put(key, value, defaultPreviousValue));
    }

    @Override
    final int removeWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }
}
