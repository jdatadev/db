package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;
import dev.jdata.db.utils.adt.sets.IMutableSet;

abstract class BaseMutableObjectToIntegerOrObjectMapTest<KEY, VALUES_ARRAY, VALUES_ADDABLE extends IAnyOrderAddable, MAP extends IMutableObjectKeyMap<KEY>>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<KEY[], VALUES_ARRAY, IMutableSet<KEY>, VALUES_ADDABLE, MAP> {

    abstract KEY integerToKey(int key);
    abstract int keyToInteger(KEY key);

    @Override
    final int getKey(KEY[] keys, int index) {

        return keyToInteger(keys[index]);
    }

    @Override
    final void getKeys(MAP map, IMutableSet<KEY> keysAddable) {

        map.keys(keysAddable);
    }
}
