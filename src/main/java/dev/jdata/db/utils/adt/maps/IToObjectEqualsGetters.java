package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IToObjectEqualsGetters<V, M extends IBaseMapView<?> & IToObjectEqualsGetters<V, M>> extends IToValueEqualsGettersMarker {

    <P1, P2, E extends Exception> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<V, M, P1, P2, E> scratchEqualsParameter) throws E;

    default <P1, P2, E extends Exception> boolean equals(P1 thisParameter, M other, P2 otherParameter, IObjectValueMapEqualityTester<V, P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        return equalsParameters(new ObjectValueMapScratchEqualsParameter<>(thisParameter, other, otherParameter, equalityTester));
    }
}
