package dev.jdata.db.utils.adt.maps;

public interface IObjectMapGetters<K, V> extends IMapGetters {

    @FunctionalInterface
    public interface ForEachKeyAndValue<K, V, P> {

        void each(K key, V value, P parameter);
    }

    <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<K, V, P> forEachKeyAndValue);

    void keysAndValues(K[] keysDst, V[] valuesDst);
}
