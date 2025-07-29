package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

abstract class BaseMutableIntToIntegerOrObjectMapTest<V, M extends IIntKeyMap & IClearable & IMapMutators> extends BaseMutableIntegerToIntegerOrObjectMapTest<int[], V, M> {

    @Override
    final int[] createKeysArray(int length) {

        return new int[length];
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
