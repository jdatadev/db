package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

public interface IIntToIntBaseStaticMapView extends IIntToIntMapView, IIntToIntBaseStaticMapGetters, IToIntEqualsGetters<IIntToIntBaseStaticMapView> {

    @Override
    default <P1, P2, E extends Exception> boolean equalsParameters(IntValueMapScratchEqualsParameter<IIntToIntBaseStaticMapView, P1, P2, E> scratchEqualsParameter) throws E {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final boolean result;

        if (getNumElements() == scratchEqualsParameter.getOther().getNumElements()) {

            result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

                final int otherValue = p.getOther().get(k);

                return p.getEqualityTester().equals(v, p.getThisParameter(), otherValue, p.getOtherParameter()) ? Boolean.TRUE : Boolean.FALSE;
            });
        }
        else {
            result = false;
        }

        return result;
    }
}
