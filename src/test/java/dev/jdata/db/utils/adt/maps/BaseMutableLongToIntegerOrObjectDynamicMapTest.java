package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

abstract class BaseMutableLongToIntegerOrObjectDynamicMapTest<

                VALUES_ARRAY,
                VALUES_ADDABLE extends IAnyOrderAddable,
                MAP extends IMutableLongKeyMap & ILongContainsKeyMapView & ILongKeyDynamicMapRemovalMutators>

        extends BaseMutableLongToIntegerOrObjectMapTest<VALUES_ARRAY, VALUES_ADDABLE, MAP> {

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
