package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IToIntEqualsGetters<M extends IBaseMapView<?> & IToIntEqualsGetters<M>> extends IToValueEqualsGettersMarker {

    <P1, P2, E extends Exception> boolean equalsParameters(IntValueMapScratchEqualsParameter<M, P1, P2, E> scratchEqualsParameter) throws E;

    default <P1, P2, E extends Exception> boolean equals(P1 thisParameter, M other, P2 otherParameter, IIntValueMapEqualityTester<P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        return equalsParameters(new IntValueMapScratchEqualsParameter<>(thisParameter, other, otherParameter, equalityTester));
    }
}
