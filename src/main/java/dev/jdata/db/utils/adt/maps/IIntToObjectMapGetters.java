package dev.jdata.db.utils.adt.maps;

public interface IIntToObjectMapGetters<T> extends IMapGetters {

    @FunctionalInterface
    public interface ForEachKeyAndValue<T, P> {

        void each(int key, T value, P parameter);
    }

    <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<T, P> forEachKeyAndValue);

    void keysAndValues(int[] keysDst, T[] valuesDst);
}
