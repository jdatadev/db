package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IIntToIntCommonMapGetters extends ICommonMapGetters {

    @FunctionalInterface
    public interface IForEachKeyAndValue<P> {

        void each(int key, int value, P parameter);
    }

    @FunctionalInterface
    public interface IForEachKeyAndValueWithResult<P, DELEGATE, R> {

        R each(int key, int value, P parameter, DELEGATE delegate);
    }

    <P, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P parameter, DELEGATE delegate, IForEachKeyAndValueWithResult<P, DELEGATE, R> forEach);

    default <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<P> forEach) {

        Objects.requireNonNull(forEach);

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(k, v, p);

            return null;
        });
    }

    void keysAndValues(int[] keysDst, int[] valuesDst);
}
