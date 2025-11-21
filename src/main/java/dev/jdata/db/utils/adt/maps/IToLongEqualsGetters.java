package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IToLongEqualsGetters<M extends IBaseMapView<?> & IToLongEqualsGetters<M>> extends IToValueEqualsGettersMarker {

    <P1, P2, E extends Exception> boolean equalsParameters(LongValueMapScratchEqualsParameter<M, P1, P2, E> scratchEqualsParameter) throws E;

    default <P1, P2, E extends Exception> boolean equals(P1 thisParameter, M other, P2 otherParameter, ILongValueMapEqualityTester<P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        return equalsParameters(new LongValueMapScratchEqualsParameter<>(thisParameter, other, otherParameter, equalityTester));
    }
}
