package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

public interface ILongToLongBaseStaticMapView extends ILongToLongMapView, ILongToLongBaseStaticMapGetters, IToLongEqualsGetters<ILongToLongBaseStaticMapView> {

    @Override
    default <P1, P2, E extends Exception> boolean equalsParameters(LongValueMapScratchEqualsParameter<ILongToLongBaseStaticMapView, P1, P2, E> scratchEqualsParameter) throws E {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final boolean result;

        if (getNumElements() == scratchEqualsParameter.getOther().getNumElements()) {

            result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

                final long otherValue = p.getOther().get(k);

                return p.getEqualityTester().equals(v, p.getThisParameter(), otherValue, p.getOtherParameter()) ? Boolean.TRUE : Boolean.FALSE;
            });
        }
        else {
            result = false;
        }

        return result;
    }
}
