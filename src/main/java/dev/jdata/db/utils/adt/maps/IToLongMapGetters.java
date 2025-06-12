package dev.jdata.db.utils.adt.maps;

interface IToLongMapGetters<M extends IKeyMap<?> & IToLongMapGetters<M>> extends IToValueMapGetters {

    @FunctionalInterface
    public interface IForEachValue<P> {

        void each(long value, P parameter);
    }

    <P> void forEachValue(P parameter, IForEachValue<P> forEach);

    <P1, P2> boolean equals(P1 thisParameter, M other, P2 otherParameter, ILongValueMapEqualityTester<P1, P2> equalityTester);
}
