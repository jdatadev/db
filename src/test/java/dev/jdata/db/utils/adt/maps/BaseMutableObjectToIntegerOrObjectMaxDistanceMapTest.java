package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.INonDistinct;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;

abstract class BaseMutableObjectToIntegerOrObjectMaxDistanceMapTest<

                KEY,
                VALUES_ARRAY,
                VALUES_ADDABLE extends IOrderedAddable<?> & INonDistinct,
                MAP extends IMutableObjectKeyMap<KEY> & IObjectContainsKeyMapView<KEY> & IObjectKeyDynamicMapRemovalMutators<KEY>>

            extends BaseMutableObjectToIntegerOrObjectMapTest<KEY, VALUES_ARRAY, VALUES_ADDABLE, MAP> {

    @Override
    final boolean supportsContainsKey() {

        return true;
    }

    @Override
    final boolean supportsRemoveNonAdded() {

        return true;
    }

    @Override
    final boolean remove(MAP map, int key) {

        return map.remove(integerToKey(key));
    }

    @Override
    final boolean containsKey(MAP map, int key) {

        return map.containsKey(integerToKey(key));
    }
}
