package dev.jdata.db.utils.adt.maps;

public final class IntValueMapScratchEqualsParameter<M extends IKeyMap<?> & IToIntMapGetters<M>, P1, P2>

        extends ValueMapScratchEqualsParameter<M, P1, P2, IIntValueMapEqualityTester<P1, P2>> {

    public IntValueMapScratchEqualsParameter() {

    }

    public IntValueMapScratchEqualsParameter(P1 thisParameter, M other, P2 otherParameter, IIntValueMapEqualityTester<P1, P2> equalityTester) {
        super(thisParameter, other, otherParameter, equalityTester);
    }
}
