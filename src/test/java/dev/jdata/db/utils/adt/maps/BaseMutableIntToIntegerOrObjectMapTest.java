package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.INonDistinct;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;
import dev.jdata.db.utils.adt.lists.IMutableIntList;

abstract class BaseMutableIntToIntegerOrObjectMapTest<VALUES_ARRAY, VALUES_ADDABLE extends IOrderedAddable<?> & INonDistinct, MAP extends IMutableIntKeyMap>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<int[], VALUES_ARRAY, IMutableIntList, VALUES_ADDABLE, MAP> {

    @Override
    final int[] createKeysArray(int length) {

        return new int[length];
    }

    @Override
    final IMutableIntList createKeysOrderedAddable(int initialCapacity) {

        return createIntOrderedAddable(initialCapacity);
    }

    @Override
    final int[] keysToArray(IMutableIntList keysAddable) {

        return toArray(keysAddable);
    }

    @Override
    final int getKey(int[] keys, int index) {

        return keys[index];
    }

    @Override
    final void getKeys(MAP map, IMutableIntList keysAddable) {

        map.keys(keysAddable);
    }
}
