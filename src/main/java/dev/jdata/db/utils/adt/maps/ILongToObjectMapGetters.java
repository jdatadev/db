package dev.jdata.db.utils.adt.maps;

public interface ILongToObjectMapGetters<T> extends IMapGetters {

    @FunctionalInterface
    public interface ForEachKeyAndValue<T, P> {

        void each(long key, T value, P parameter);
    }

    <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<T, P> forEachKeyAndValue);

    void keysAndValues(long[] keysDst, T[] valuesDst);
}
