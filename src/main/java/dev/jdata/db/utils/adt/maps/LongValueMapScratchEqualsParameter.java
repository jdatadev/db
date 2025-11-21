package dev.jdata.db.utils.adt.maps;

public final class LongValueMapScratchEqualsParameter<M extends IBaseMapView<?> & IToLongEqualsGetters<M>, P1, P2, E extends Exception>

        extends ValueMapScratchEqualsParameter<M, P1, P2, ILongValueMapEqualityTester<P1, P2, E>, E> {

    public LongValueMapScratchEqualsParameter() {

    }

    public LongValueMapScratchEqualsParameter(P1 thisParameter, M other, P2 otherParameter, ILongValueMapEqualityTester<P1, P2, E> equalityTester) {
        super(thisParameter, other, otherParameter, equalityTester);
    }
}
