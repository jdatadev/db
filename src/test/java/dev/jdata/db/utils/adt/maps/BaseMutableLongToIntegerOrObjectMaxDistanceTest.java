package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseMutableLongToIntegerOrObjectMaxDistanceTest<V, M extends ILongContainsKeyMap & IClearable & ILongKeyMapRemovalMutators>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<long[], V, M> {

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
    final int getKey(long[] keys, int index) {

        return Integers.checkLongToInt(keys[index]);
    }

    @Override
    final int[] getKeys(M map) {

        return Array.checkToIntArray(map.keys());
    }
}
