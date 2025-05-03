package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

abstract class BaseMutableIntToIntegerOrObjectMaxDistanceTest<V, M extends IIntContainsKeyMap & IClearable & IIntKeyMapRemovalMutators>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<int[], V, M> {

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

        return map.remove(key);
    }

    @Override
    final boolean containsKey(M map, int key) {

        return map.containsKey(key);
    }

    @Override
    final int getKey(int[] keys, int index) {

        return keys[index];
    }

    @Override
    final int[] getKeys(M map) {

        return map.keys();
    }
}
