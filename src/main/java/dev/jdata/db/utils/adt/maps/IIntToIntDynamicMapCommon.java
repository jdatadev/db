package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

interface IIntToIntDynamicMapCommon extends IBaseIntToIntMapCommon<IIntToIntDynamicMapCommon>, IIntContainsKeyMap, IIntToIntDynamicMapGetters {

    @Override
    default <P1, P2> boolean equals(P1 thisParameter, IIntToIntDynamicMapCommon other, P2 otherParameter, IIntValueMapEqualityTester<P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        return equalsParameters(new IntValueMapScratchEqualsParameter<>(thisParameter, other, otherParameter, equalityTester));
    }

    default <P1, P2> boolean equalsParameters(IntValueMapScratchEqualsParameter<IIntToIntDynamicMapCommon, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final Boolean result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

            final Boolean eachResult;

            final IIntToIntDynamicMapCommon other = p.getOther();

            if (other.containsKey(k)) {

                final int otherValue = other.get(k, -1);

                eachResult = p.getEqualityTester().equals(v, p.getThisParameter(), otherValue, p.getOtherParameter()) ? Boolean.TRUE : Boolean.FALSE;
            }
            else {
                eachResult = Boolean.FALSE;
            }

            return eachResult;
        });

        return result.booleanValue();
    }
}
