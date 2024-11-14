package dev.jdata.db.test.unit.assertj;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jdata.db.utils.adt.KeySetElements;

abstract class BaseKeySetElementsAssert<S extends BaseKeySetElementsAssert<S, A, K>, A extends KeySetElements<K>, K>
        extends BaseKeyElementsAssert<S, A>
        implements IKeySetElementsAssert<S, K> {

    BaseKeySetElementsAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    @SafeVarargs
    @Override
    public final S hasUnmodifiableKeySet(K... keys) {

        isNotNull();

        assertThat(actual.unmdifiableKeySet()).containsExactlyInAnyOrder(keys);

        return getThis();
    }
}
