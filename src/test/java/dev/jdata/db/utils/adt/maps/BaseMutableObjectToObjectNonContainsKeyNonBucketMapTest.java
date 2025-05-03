package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.IClearable;

abstract class BaseMutableObjectToObjectNonContainsKeyNonBucketMapTest<

                K,
                V,
                M extends IMutableMap<K, V> & IClearable & IObjectMapMutators<K, V> & IObjectMapGetters<K, V> & IObjectNonContainsKeyNonBucketMapGetters<K, V>>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<K[], V[], M> {

    abstract int objectKeyToInt(K object);
    abstract K intKeyToObject(int integer);

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
    final <P> void forEachKeysAndValues(M map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    final <P> void forEachKeysAndValues(M map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(objectKeyToInt(k));
            valuesDst.add(objectValueToInt(v));
            parameters.add(p);
        });
    }

    @Override
    final void keysAndValues(M map, K[] keysDst, V[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    final int get(M map, int key) {

        return objectValueToInt(map.get(intKeyToObject(key)));
    }

    @Override
    final int getWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }

    @Override
    final int put(M map, int key, int value, int defaultPreviousValue) {

        return objectValueToInt(map.put(intKeyToObject(key), intValueToObject(value), intValueToObject(defaultPreviousValue)));
    }

    @Override
    final int removeWithDefaultValue(M map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }
}
