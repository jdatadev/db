package dev.jdata.db.utils.adt.maps;

interface IObjectToObjectCommonMapGetters<K, V> extends ICommonMapGetters {

    @FunctionalInterface
    public interface IForEachKeyAndValue<K, V, P> {

        void each(K key, V value, P parameter);
    }

    @FunctionalInterface
    public interface IForEachKeyAndValueWithResult<K, V, P, DELEGATE, R> {

        R each(K key, V value, P parameter, DELEGATE delegate);
    }

    <P, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P parameter, DELEGATE delegate, IForEachKeyAndValueWithResult<K, V, P, DELEGATE, R> forEach);

    default <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<K, V, P> forEach) {

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(k, v, p);

            return null;
        });
    }

    void keysAndValues(K[] keysDst, V[] valuesDst);
}
