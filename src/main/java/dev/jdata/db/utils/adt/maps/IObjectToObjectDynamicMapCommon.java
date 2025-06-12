package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

public interface IObjectToObjectDynamicMapCommon<K, V>

        extends IBaseObjectToObjectMapCommon<K, V, IObjectToObjectDynamicMapCommon<K, V>>, IObjectToObjectDynamicMapGetters<K, V> {

    @Override
    default <P1, P2> boolean equals(P1 thisParameter, IObjectToObjectDynamicMapCommon<K, V> other, P2 otherParameter, IObjectValueMapEqualityTester<V, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        return equalsParameters(new ObjectValueMapScratchEqualsParameter<>(thisParameter, other, otherParameter, equalityTester));
    }

    default <P1, P2> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<V, IObjectToObjectDynamicMapCommon<K, V>, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final Boolean result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

            final Boolean eachResult;

            final V defaultValue = p.getDefaultValue();
            final V otherValue = p.getOther().get(k, defaultValue);

            if (otherValue == defaultValue) {

                eachResult = Boolean.FALSE;
            }
            else {
                eachResult = p.getEqualityTester().equals(v, p.getThisParameter(), otherValue, p.getOtherParameter()) ? Boolean.TRUE : Boolean.FALSE;
            }

            return eachResult;
        });

        return result.booleanValue();
    }
}
