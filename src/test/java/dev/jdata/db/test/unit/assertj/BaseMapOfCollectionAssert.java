package dev.jdata.db.test.unit.assertj;

import java.util.Collection;

import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;
import dev.jdata.db.utils.adt.maps.IMapOfCollection;

abstract class BaseMapOfCollectionAssert<S extends BaseMapOfCollectionAssert<S, A, K, V, C>, A extends IMapOfCollection<K, V, C>, K, V, C extends Collection<V>>

        extends BaseMapAssert<K[], IObjectAnyOrderAddable<K>, S, A>
        implements IKeySetElementsAssert<S, K> {

    BaseMapOfCollectionAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }
}
