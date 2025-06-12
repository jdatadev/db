package dev.jdata.db.utils.adt.maps;

public final class ObjectValueMapScratchEqualsParameter<T, M extends IKeyMap<?> & IToObjectMapGetters<T, M>, P1, P2>

        extends ValueMapScratchEqualsParameter<M, P1, P2, IObjectValueMapEqualityTester<T, P1, P2>> {

    private T defaultValue;

    public ObjectValueMapScratchEqualsParameter() {

        initialize();
    }

    public ObjectValueMapScratchEqualsParameter(P1 thisParameter, M other, P2 otherParameter, IObjectValueMapEqualityTester<T, P1, P2> equalityTester) {
        super(thisParameter, other, otherParameter, equalityTester);

        initialize();
    }

    T getDefaultValue() {
        return defaultValue;
    }

    private void initialize() {

        @SuppressWarnings("unchecked")
        final T defaultValue = (T)new Object();

        this.defaultValue = defaultValue;
    }
}
