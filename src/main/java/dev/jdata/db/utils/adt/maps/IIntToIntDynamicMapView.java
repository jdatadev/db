package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

public interface IIntToIntDynamicMapView

        extends IIntToIntMapView, IDynamicMapView, IIntContainsKeyMapView, IIntToIntDynamicMapGetters, IToIntEqualsGetters<IIntToIntDynamicMapView> {

    @Override
    default <P1, P2, E extends Exception> boolean equalsParameters(IntValueMapScratchEqualsParameter<IIntToIntDynamicMapView, P1, P2, E> scratchEqualsParameter) throws E {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final boolean result;

        if (getNumElements() == scratchEqualsParameter.getOther().getNumElements()) {

            result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

                final Boolean eachResult;

                final IIntToIntDynamicMapView other = p.getOther();

                if (other.containsKey(k)) {

                    final int otherValue = other.get(k, -1);

                    eachResult = p.getEqualityTester().equals(v, p.getThisParameter(), otherValue, p.getOtherParameter()) ? Boolean.TRUE : Boolean.FALSE;
                }
                else {
                    eachResult = Boolean.FALSE;
                }

                return eachResult;
            });
        }
        else {
            result = false;
        }

        return result;
    }
}
