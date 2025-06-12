package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.arrays.Array;

abstract class BaseMutableObjectToIntegerOrObjectMaxDistanceMapTest<K, V, M extends IObjectContainsKeyMap<K> & IClearable & IObjectKeyDynamicMapRemovalMutators<K>>

            extends BaseMutableObjectToIntegerOrObjectMapTest<K, V[], M> {

    abstract K integerToKey(int key);
    abstract int keyToInteger(K key);

    @Override
    final boolean supportsContainsKey() {

        return true;
    }

    @Override
    final boolean supportsRemoveNonAdded() {

        return true;
    }

    @Override
    final boolean remove(M map, int key) {

        return map.remove(integerToKey(key));
    }

    @Override
    final boolean containsKey(M map, int key) {

        return map.containsKey(integerToKey(key));
    }

    @Override
    final int getKey(K[] keys, int index) {

        return keyToInteger(keys[index]);
    }

    @Override
    final int[] getKeys(M map) {

        final K[] keys = map.keys();

        return Array.mapToInt(keys, this, (e, i) -> i.keyToInteger(e));
    }
}
