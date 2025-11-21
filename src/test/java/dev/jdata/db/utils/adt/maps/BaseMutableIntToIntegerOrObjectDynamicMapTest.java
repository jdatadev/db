package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.INonDistinct;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;

abstract class BaseMutableIntToIntegerOrObjectDynamicMapTest<

                VALUES_ARRAY,
                VALUES_ADDABLE extends IOrderedAddable<?> & INonDistinct,
                MAP extends IMutableIntKeyMap & IIntContainsKeyMapView & IIntKeyDynamicMapRemovalMutators>

        extends BaseMutableIntToIntegerOrObjectMapTest<VALUES_ARRAY, VALUES_ADDABLE, MAP> {

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

        return map.remove(key);
    }

    @Override
    final boolean containsKey(MAP map, int key) {

        return map.containsKey(key);
    }
}
