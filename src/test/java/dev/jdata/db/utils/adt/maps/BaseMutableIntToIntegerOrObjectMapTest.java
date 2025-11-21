package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;

abstract class BaseMutableIntToIntegerOrObjectMapTest<VALUES_ARRAY, VALUES_ADDABLE extends IAnyOrderAddable, MAP extends IMutableIntKeyMap>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<int[], VALUES_ARRAY, IMutableIntSet, VALUES_ADDABLE, MAP> {

    @Override
    final int[] createKeysArray(int length) {

        return new int[length];
    }

    @Override
    final IMutableIntSet createKeysAddable(int initialCapacity) {

        return createIntAddable(initialCapacity);
    }

    @Override
    final int[] keysToArray(IMutableIntSet keysAddable) {

        return toArray(keysAddable);
    }

    @Override
    final int getKey(int[] keys, int index) {

        return keys[index];
    }

    @Override
    final void getKeys(MAP map, IMutableIntSet keysAddable) {

        map.keys(keysAddable);
    }
}
