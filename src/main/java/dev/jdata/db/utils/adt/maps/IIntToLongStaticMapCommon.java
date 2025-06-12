package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

interface IIntToLongStaticMapCommon extends IBaseIntToLongMapCommon<IIntToLongStaticMapCommon>, IIntToLongStaticMapGetters {

    @Override
    default <P1, P2> boolean equals(P1 thisParameter, IIntToLongStaticMapCommon other, P2 otherParameter, ILongValueMapEqualityTester< P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        return equalsParameters(new LongValueMapScratchEqualsParameter<>(thisParameter, other, otherParameter, equalityTester));
    }

    default <P1, P2> boolean equalsParameters(LongValueMapScratchEqualsParameter<IIntToLongStaticMapCommon, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final Boolean result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

            final long otherValue = p.getOther().get(k);

            return p.getEqualityTester().equals(v, p.getThisParameter(), otherValue, p.getOtherParameter()) ? Boolean.TRUE : Boolean.FALSE;
        });

        return result.booleanValue();
    }
}
