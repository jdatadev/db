package dev.jdata.db.test.unit.assertj;

import java.util.Collection;

import dev.jdata.db.utils.adt.maps.IMapOfCollection;

abstract class BaseMapOfCollectionAssert<S extends BaseMapOfCollectionAssert<S, A, K, V, C>, A extends IMapOfCollection<K, V, C>, K, V, C extends Collection<V>>
        extends BaseElementsAssert<S, A>
        implements IKeySetElementsAssert<S, K> {

    private final KeySetElementsAssert<K> keySetElementsAssert;

    BaseMapOfCollectionAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);

        this.keySetElementsAssert = new KeySetElementsAssert<>(actual);
    }

    @Override
    public final S hasNumKeys(int expectedNumKeys) {

        keySetElementsAssert.hasNumKeys(expectedNumKeys);

        return getThis();
    }

    @SafeVarargs
    @Override
    public final S hasUnmodifiableKeySet(K... keys) {

        keySetElementsAssert.hasUnmodifiableKeySet(keys);

        return getThis();
    }
}
