package dev.jdata.db.utils.adt.maps;

public interface IToObjectMapGetters<T, M extends IKeyMap<?> & IToObjectMapGetters<T, M>> extends IToValueMapGetters {

    @FunctionalInterface
    public interface IForEachValue<T, P> {

        void each(T value, P parameter);
    }

    <P> void forEachValue(P parameter, IForEachValue<T, P> forEach);

    <P1, P2> boolean equals(P1 thisParameter, M other, P2 otherParameter, IObjectValueMapEqualityTester<T, P1, P2> equalityTester);
}
