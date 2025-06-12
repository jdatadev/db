package dev.jdata.db.utils.adt.maps;

interface IToIntMapGetters<M extends IKeyMap<?> & IToIntMapGetters<M>> extends IToValueMapGetters {

    @FunctionalInterface
    public interface IForEachValue<P> {

        void each(int value, P parameter);
    }

    <P> void forEachValue(P parameter, IForEachValue<P> forEach);

    <P1, P2> boolean equals(P1 thisParameter, M other, P2 otherParameter, IIntValueMapEqualityTester<P1, P2> equalityTester);
}
