package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

public interface ILongToObjectDynamicMapCommon<T>

        extends IBaseLongToObjectMapCommon<T, ILongToObjectDynamicMapCommon<T>>, ILongContainsKeyMap, ILongToObjectDynamicMapGetters<T> {

    @Override
    default <P1, P2> boolean equals(P1 thisParameter, ILongToObjectDynamicMapCommon<T> other, P2 otherParameter, IObjectValueMapEqualityTester<T, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        return equalsParameters(new ObjectValueMapScratchEqualsParameter<>(thisParameter, other, otherParameter, equalityTester));
    }

    default <P1, P2> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<T, ILongToObjectDynamicMapCommon<T>, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final Boolean result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

            final Boolean eachResult;

            final T defaultValue = p.getDefaultValue();
            final T otherValue = p.getOther().get(k, defaultValue);

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
