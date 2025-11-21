package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableSet;

abstract class BaseMutableIntToObjectNonContainsKeyNonBucketMapTest<V, M extends IMutableIntToObjectMap<V> & IIntToObjectBaseStaticMapCommon<V> & IIntToObjectStoreMapMutators<V>>

        extends BaseMutableIntToIntegerOrObjectNonBucketMapTest<V[], IMutableSet<V>, M> {

    abstract int objectToInt(V object);
    abstract V intToObject(int integer);

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
    final int getValue(V[] values, int index) {

        return objectToInt(values[index]);
    }

    @Override
    final <P> void forEachKeyAndValueWithNullFunction(M map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    final <P> void forEachKeyAndValue(M map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(k);
            valuesDst.add(objectToInt(v));
            parameters.add(p);
        });
    }

    @Override
    final void keysAndValues(M map, IMutableIntSet keysAddable, IMutableSet<V> valuesAddable) {

        map.keysAndValues(keysAddable, valuesAddable);
    }

    @Override
    final int get(M map, int key) {

        return objectToInt(map.get(key));
    }

    @Override
    final int getWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }

    @Override
    final int put(M map, int key, int value, int defaultPreviousValue) {

        return objectToInt(map.put(key, intToObject(value), intToObject(defaultPreviousValue)));
    }

    @Override
    final int removeWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }
}
