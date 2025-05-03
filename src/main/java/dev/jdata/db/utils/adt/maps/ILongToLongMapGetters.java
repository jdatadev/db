package dev.jdata.db.utils.adt.maps;

public interface ILongToLongMapGetters extends IMapGetters {

    @FunctionalInterface
    public interface ForEachKeyAndValue<P> {

        void each(long key, long value, P parameter);
    }

    <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<P> forEachKeyAndValue);

    void keysAndValues(long[] keysDst, long[] valuesDst);
}
