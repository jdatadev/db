package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseMutableLongToIntegerOrObjectNonBucketMapTest<V, M extends ILongKeyMap & IClearable & IMapMutators>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<long[], V, M> {

    @Override
    final long[] createKeysArray(int length) {

        return new long[length];
    }

    @Override
    final int getKey(long[] keys, int index) {

        return Integers.checkUnsignedLongToUnsignedInt(keys[index]);
    }

    @Override
    final int[] getKeys(M map) {

        return Array.checkToUnsignedIntArray(map.keys());
    }
}
