package dev.jdata.db.utils.adt.maps;

public interface IToObjectMapGetters<T> {

    @FunctionalInterface
    public interface ForEachValue<T, P> {

        void each(T value, P parameter);
    }

    <P> void forEachValue(P parameter, ForEachValue<T, P> forEachValue);
}
