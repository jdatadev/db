package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;
import dev.jdata.db.utils.adt.sets.IHeapMutableLongSet;
import dev.jdata.db.utils.adt.sets.IMutableLongSet;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseMutableLongToIntegerOrObjectMapTest<VALUES_ARRAY, VALUES_ADDABLE extends IAnyOrderAddable, MAP extends IMutableLongKeyMap>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<long[], VALUES_ARRAY, IMutableLongSet, VALUES_ADDABLE, MAP> {

    @Override
    final long[] createKeysArray(int length) {

        return new long[length];
    }

    @Override
    final IMutableLongSet createKeysAddable(int initialCapacity) {

        return IHeapMutableLongSet.create(initialCapacity);
    }

    @Override
    final long[] keysToArray(IMutableLongSet keysAddable) {

        return toArray(keysAddable);
    }

    @Override
    int getKey(long[] keys, int index) {

        return Integers.checkLongToInt(keys[index]);
    }

    @Override
    final void getKeys(MAP map, IMutableLongSet keysAddable) {

        map.keys(keysAddable);
    }
}
