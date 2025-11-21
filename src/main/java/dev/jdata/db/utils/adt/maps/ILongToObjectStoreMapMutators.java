package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface ILongToObjectStoreMapMutators<V> extends IStoreMapMutatorsMarker {

    V put(long key, V value, V defaultPreviousValue);

    default void put(long key, V value) {

        put(key, value, null);
    }

    default void putAll(ILongToObjectDynamicMapView<V> map) {

        Objects.requireNonNull(map);

        map.forEachKeyAndValue(this, (k, v, m) -> m.put(k, v));
    }
}
