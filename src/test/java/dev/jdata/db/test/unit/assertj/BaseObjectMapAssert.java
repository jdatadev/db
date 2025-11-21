package dev.jdata.db.test.unit.assertj;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;
import dev.jdata.db.utils.adt.maps.IBaseMapView;

abstract class BaseObjectMapAssert<K, S extends BaseObjectMapAssert<K, S, A>, A extends IBaseMapView<IObjectAnyOrderAddable<K>>>

        extends BaseMapAssert<K, IObjectAnyOrderAddable<K>, S, A> {

    BaseObjectMapAssert(A actual, Class<S> assertClass) {
        super(actual, assertClass);
    }

    @SafeVarargs
    public final S hasUnmodifiableKeySet(K ... keys) {

        isNotNull();

        final Set<K> keySet = new HashSet<>(keys.length);

        actual.keys(keySet::add);

        assertThat(keySet).containsExactlyInAnyOrder(keys);

        return getThis();
    }
}
