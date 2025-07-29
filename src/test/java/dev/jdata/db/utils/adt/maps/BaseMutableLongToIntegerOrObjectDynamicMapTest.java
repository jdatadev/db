package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;

abstract class BaseMutableLongToIntegerOrObjectDynamicMapTest<V, M extends ILongContainsKeyMap & IClearable & ILongKeyDynamicMapRemovalMutators>

        extends BaseMutableLongToIntegerOrObjectMapTest<V, M> {

    @Override
    final boolean supportsContainsKey() {

        return true;
    }

    @Override
    final boolean supportsRemoveNonAdded() {

        return true;
    }

    @Override
    final boolean remove(M map, int key) {

        return map.remove(key);
    }

    @Override
    final boolean containsKey(M map, int key) {

        return map.containsKey(key);
    }
}
