package dev.jdata.db.utils.adt.maps;

public interface IIntToLongMapGetters extends IMapGetters {

    @FunctionalInterface
    public interface ForEachKeyAndValue<P> {

        void each(int key, long value, P parameter);
    }

    <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<P> forEachKeyAndValue);

    void keysAndValues(int[] keysDst, long[] valuesDst);
}
