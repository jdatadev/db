package dev.jdata.db.utils.adt.maps;

public interface IIntToIntMapGetters extends IMapGetters {

    @FunctionalInterface
    public interface ForEachKeyAndValue<P> {

        void each(int key, int value, P parameter);
    }

    <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<P> forEachKeyAndValue);

    void keysAndValues(int[] keysDst, int[] valuesDst);
}
