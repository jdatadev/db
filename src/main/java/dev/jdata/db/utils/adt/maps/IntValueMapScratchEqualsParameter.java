package dev.jdata.db.utils.adt.maps;

public final class IntValueMapScratchEqualsParameter<M extends IBaseMapView<?> & IToIntEqualsGetters<M>, P1, P2, E extends Exception>

        extends ValueMapScratchEqualsParameter<M, P1, P2, IIntValueMapEqualityTester<P1, P2, E>, E> {

    public IntValueMapScratchEqualsParameter() {

    }

    public IntValueMapScratchEqualsParameter(P1 thisParameter, M other, P2 otherParameter, IIntValueMapEqualityTester<P1, P2, E> equalityTester) {
        super(thisParameter, other, otherParameter, equalityTester);
    }
}
