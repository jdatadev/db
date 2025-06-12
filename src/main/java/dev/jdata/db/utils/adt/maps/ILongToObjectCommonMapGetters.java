package dev.jdata.db.utils.adt.maps;

public interface ILongToObjectCommonMapGetters<T> extends ICommonMapGetters {

    @FunctionalInterface
    public interface IForEachKeyAndValue<T, P> {

        void each(long key, T value, P parameter);
    }

    @FunctionalInterface
    public interface IForEachKeyAndValueWithResult<T, P, DELEGATE, R> {

        R each(long key, T value, P parameter, DELEGATE delegate);
    }

    <P, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P parameter, DELEGATE delegate, IForEachKeyAndValueWithResult<T, P, DELEGATE, R> forEach);

    default <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<T, P> forEach) {

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(k, v, p);

            return null;
        });
    }

    void keysAndValues(long[] keysDst, T[] valuesDst);
}
