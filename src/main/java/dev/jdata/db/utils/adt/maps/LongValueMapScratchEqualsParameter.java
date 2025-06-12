package dev.jdata.db.utils.adt.maps;

public final class LongValueMapScratchEqualsParameter<M extends IKeyMap<?> & IToLongMapGetters<M>, P1, P2>

        extends ValueMapScratchEqualsParameter<M, P1, P2, ILongValueMapEqualityTester<P1, P2>> {

    public LongValueMapScratchEqualsParameter() {

    }

    public LongValueMapScratchEqualsParameter(P1 thisParameter, M other, P2 otherParameter, ILongValueMapEqualityTester<P1, P2> equalityTester) {
        super(thisParameter, other, otherParameter, equalityTester);
    }
}
