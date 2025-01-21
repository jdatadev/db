package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLongToIntegerOrObjectTest<V, M extends LongKeyMap> extends BaseIntegerToIntegerOrObjectTest<long[], V, M> {

    @Override
    final long[] createKeysArray(int length) {

        return new long[length];
    }

    @Override
    final int getKey(long[] keys, int index) {

        return Integers.checkUnsignedLongToUnsignedInt(keys[index]);
    }

    @Override
    final boolean contains(M map, int key) {

        return map.containsKey(key);
    }

    @Override
    final int[] getKeys(M map) {

        return Array.checkToUnsignedIntArray(map.keys());
    }

    @Override
    final boolean remove(M map, int key) {

        return map.remove(key);
    }
}
