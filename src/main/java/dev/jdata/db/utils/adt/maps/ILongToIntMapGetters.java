package dev.jdata.db.utils.adt.maps;

public interface ILongToIntMapGetters extends IMapGetters {

    @FunctionalInterface
    public interface ForEachKeyAndValue<P> {

        void each(long key, int value, P parameter);
    }

    <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<P> forEachKeyAndValue);

    void keysAndValues(long[] keysDst, int[] valuesDst);
}
