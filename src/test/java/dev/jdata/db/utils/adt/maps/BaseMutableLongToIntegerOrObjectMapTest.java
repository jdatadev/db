package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.INonDistinct;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;
import dev.jdata.db.utils.adt.lists.IHeapMutableLongIndexList;
import dev.jdata.db.utils.adt.lists.IMutableLongList;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseMutableLongToIntegerOrObjectMapTest<VALUES_ARRAY, VALUES_ADDABLE extends IOrderedAddable<?> & INonDistinct, MAP extends IMutableLongKeyMap>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<long[], VALUES_ARRAY, IMutableLongList, VALUES_ADDABLE, MAP> {

    @Override
    final long[] createKeysArray(int length) {

        return new long[length];
    }

    @Override
    final IMutableLongList createKeysOrderedAddable(int initialCapacity) {

        return IHeapMutableLongIndexList.create(initialCapacity);
    }

    @Override
    final long[] keysToArray(IMutableLongList keysAddable) {

        return toArray(keysAddable);
    }

    @Override
    int getKey(long[] keys, int index) {

        return Integers.checkLongToInt(keys[index]);
    }

    @Override
    final void getKeys(MAP map, IMutableLongList keysAddable) {

        map.keys(keysAddable);
    }
}
