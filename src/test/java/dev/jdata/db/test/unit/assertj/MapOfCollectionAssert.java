package dev.jdata.db.test.unit.assertj;

import java.util.Collection;

import dev.jdata.db.utils.adt.maps.IMapOfCollection;

public final class MapOfCollectionAssert<K, V, C extends Collection<V>>
        extends BaseMapOfCollectionAssert<MapOfCollectionAssert<K, V, C>, IMapOfCollection<K, V, C>, K, V, C> {

    MapOfCollectionAssert(IMapOfCollection<K, V, C> actual) {
        super(actual, castAssertClass(MapOfCollectionAssert.class));
    }
}
