package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.INonDistinct;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;
import dev.jdata.db.utils.adt.lists.IMutableList;

abstract class BaseMutableObjectToIntegerOrObjectMapTest<KEY, VALUES_ARRAY, VALUES_ADDABLE extends IOrderedAddable<?> & INonDistinct, MAP extends IMutableObjectKeyMap<KEY>>

        extends BaseMutableIntegerToIntegerOrObjectMapTest<KEY[], VALUES_ARRAY, IMutableList<KEY>, VALUES_ADDABLE, MAP> {

    abstract KEY integerToKey(int key);
    abstract int keyToInteger(KEY key);

    @Override
    final int getKey(KEY[] keys, int index) {

        return keyToInteger(keys[index]);
    }

    @Override
    final void getKeys(MAP map, IMutableList<KEY> keysAddable) {

        map.keys(keysAddable);
    }
}
