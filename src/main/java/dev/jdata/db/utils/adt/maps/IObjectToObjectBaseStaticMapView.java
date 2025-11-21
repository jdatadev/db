package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

public interface IObjectToObjectBaseStaticMapView<K, V>

        extends IObjectToObjectMapView<K, V>, IObjectToObjectBaseStaticMapGetters<K, V>, IToObjectEqualsGetters<V, IObjectToObjectBaseStaticMapView<K, V>> {

    @Override
    default <P1, P2, E extends Exception> boolean equalsParameters(
            ObjectValueMapScratchEqualsParameter<V, IObjectToObjectBaseStaticMapView<K, V>, P1, P2, E> scratchEqualsParameter) throws E {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final boolean result;

        if (getNumElements() == scratchEqualsParameter.getOther().getNumElements()) {

            result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

                final V otherValue = p.getOther().get(k);

                return p.getEqualityTester().equals(v, p.getThisParameter(), otherValue, p.getOtherParameter()) ? Boolean.TRUE : Boolean.FALSE;
            });
        }
        else {
            result = false;
        }

        return result;
    }
}
