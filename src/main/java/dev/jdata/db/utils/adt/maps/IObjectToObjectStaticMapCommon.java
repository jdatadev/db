package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

interface IObjectToObjectStaticMapCommon<K, V> extends IBaseObjectToObjectMapCommon<K, V, IObjectToObjectStaticMapCommon<K, V>>, IObjectStaticMapGetters<K, V> {

    @Override
    default <P1, P2> boolean equals(P1 thisParameter, IObjectToObjectStaticMapCommon<K, V> other, P2 otherParameter, IObjectValueMapEqualityTester<V, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        return equalsParameters(new ObjectValueMapScratchEqualsParameter<>(thisParameter, other, otherParameter, equalityTester));
    }

    default <P1, P2> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<V, IObjectToObjectStaticMapCommon<K, V>, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final Boolean result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

            final V otherValue = p.getOther().get(k);

            return p.getEqualityTester().equals(v, p.getThisParameter(), otherValue, p.getOtherParameter()) ? Boolean.TRUE : Boolean.FALSE;
        });

        return result.booleanValue();
    }
}
