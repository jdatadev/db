package dev.jdata.db.utils.adt.maps;

public final class ObjectValueMapScratchEqualsParameter<V, M extends IBaseMapView<?> & IToObjectEqualsGetters<V, M>, P1, P2, E extends Exception>

        extends ValueMapScratchEqualsParameter<M, P1, P2, IObjectValueMapEqualityTester<V, P1, P2, E>, E> {

    private V defaultValue;

    public ObjectValueMapScratchEqualsParameter() {

        initialize();
    }

    public ObjectValueMapScratchEqualsParameter(P1 thisParameter, M other, P2 otherParameter, IObjectValueMapEqualityTester<V, P1, P2, E> equalityTester) {
        super(thisParameter, other, otherParameter, equalityTester);

        initialize();
    }

    V getDefaultValue() {
        return defaultValue;
    }

    private void initialize() {

        @SuppressWarnings("unchecked")
        final V defaultValue = (V)new Object();

        this.defaultValue = defaultValue;
    }
}
