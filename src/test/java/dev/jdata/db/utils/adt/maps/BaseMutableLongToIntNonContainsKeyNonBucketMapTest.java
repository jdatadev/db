package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseMutableLongToIntNonContainsKeyNonBucketMapTest<

                M extends ILongToIntMap & IClearable & ILongToIntMapMutators & ILongToIntNonContainsNonBucketMapGetters>

        extends BaseMutableLongToIntegerOrObjectNonBucketMapTest<int[], M> {

    @Override
    final int[] createValuesArray(int length) {

        return new int[length];
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
    final int getWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }

    @Override
    final int getValue(int[] values, int index) {

        return values[index];
    }

    @Override
    final <P> void forEachKeysAndValues(M map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    final <P> void forEachKeysAndValues(M map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(Integers.checkLongToInt(k));
            valuesDst.add(v);
            parameters.add(p);
        });
    }

    @Override
    final void keysAndValues(M map, long[] keysDst, int[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    final int get(M map, int key) {

        return map.get(key);
    }

    @Override
    final int put(M map, int key, int value, int defaultPreviousValue) {

        return map.put(key, value, defaultPreviousValue);
    }

    @Override
    final int removeWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }
}
