package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.Initializable;

public interface IObjectToObjectDynamicMapView<K, V>

        extends IObjectToObjectMapView<K, V>, IDynamicMapView, IObjectToObjectDynamicMapGetters<K, V>, IToObjectEqualsGetters<V, IObjectToObjectDynamicMapView<K, V>> {

    @Override
    default <P1, P2, E extends Exception> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<V, IObjectToObjectDynamicMapView<K, V>, P1, P2, E> scratchEqualsParameter)
            throws E {

        Objects.requireNonNull(scratchEqualsParameter);
        Initializable.checkIsInitialized(scratchEqualsParameter.isInitialized());

        final boolean result;

        if (getNumElements() == scratchEqualsParameter.getOther().getNumElements()) {

            result = forEachKeyAndValueWithResult(Boolean.TRUE, scratchEqualsParameter, null, (k, v, p, d) -> {

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
        }
        else {
            result = false;
        }

        return result;
    }
}
