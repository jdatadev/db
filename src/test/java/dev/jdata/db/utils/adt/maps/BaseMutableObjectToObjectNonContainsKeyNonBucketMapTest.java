package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.sets.IMutableSet;

abstract class BaseMutableObjectToObjectNonContainsKeyNonBucketMapTest<

                K,
                V,
                M extends IMutableMap<K, V> & IObjectToObjectStoreMapMutators<K, V> & IObjectToObjectMapGetters<K, V> & IObjectToObjectBaseStaticMapGetters<K, V>>

        extends BaseMutableObjectToIntegerOrObjectMapTest<K, V[], IMutableSet<V>, M> {

    abstract int objectValueToInt(V object);
    abstract V intValueToObject(int integer);

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

        return objectValueToInt(values[index]);
    }

    @Override
    final <P> void forEachKeyAndValueWithNullFunction(M map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    final <P> void forEachKeyAndValue(M map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(keyToInteger(k));
            valuesDst.add(objectValueToInt(v));
            parameters.add(p);
        });
    }

    @Override
    final void keysAndValues(M map, IMutableSet<K> keysAddable, IMutableSet<V> valuesAddable) {

        map.keysAndValues(keysAddable, valuesAddable);
    }

    @Override
    final int get(M map, int key) {

        return objectValueToInt(map.get(integerToKey(key)));
    }

    @Override
    final int getWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }

    @Override
    final int put(M map, int key, int value, int defaultPreviousValue) {

        return objectValueToInt(map.put(integerToKey(key), intValueToObject(value), intValueToObject(defaultPreviousValue)));
    }

    @Override
    final int removeWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }
}
