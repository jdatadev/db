package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

interface ILongToIntStaticMapCommon extends IBaseLongToIntMapCommon<ILongToIntStaticMapCommon>, ILongToIntStaticMapGetters, IToIntMapGetters<ILongToIntStaticMapCommon> {

    @Override
    default <P1, P2> boolean equals(P1 thisParameter, ILongToIntStaticMapCommon other, P2 otherParameter, IIntValueMapEqualityTester< P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        return equalsParameters(new IntValueMapScratchEqualsParameter<>(thisParameter, other, otherParameter, equalityTester));
    }

    default <P1, P2> boolean equalsParameters(IntValueMapScratchEqualsParameter<ILongToIntStaticMapCommon, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final Boolean result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

            final int otherValue = p.getOther().get(k);

            return p.getEqualityTester().equals(v, p.getThisParameter(), otherValue, p.getOtherParameter()) ? Boolean.TRUE : Boolean.FALSE;
        });

        return result.booleanValue();
    }
}
