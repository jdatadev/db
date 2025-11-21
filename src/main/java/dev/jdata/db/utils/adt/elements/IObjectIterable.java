package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.function.CheckedExceptionConsumer;

interface IObjectIterable<T> extends IElementsIterable {

    default long closureOrConstantCount(Predicate<T> predicate) {

        return count(predicate, (e, p) -> p.test(e));
    }

    <P> long count(P parameter, BiPredicate<T, P> predicate);

    int maxInt(int defaultValue, ToIntFunction<? super T> mapper);
    long maxLong(long defaultValue, ToLongFunction<? super T> mapper);

    <P> T findAtMostOne(P parameter, BiPredicate<T, P> predicate);
    <P> T findExactlyOne(P parameter, BiPredicate<T, P> predicate);

    T[] toArray(IntFunction<T[]> createArray);

    <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IObjectForEachWithResult<T, P1, P2, R, E> forEach) throws E;

    default <P, E extends Exception> void forEach(P parameter, IObjectForEach<T, P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEachWithResult(null, parameter, forEach, (e, p, f) -> {

            f.each(e, p);

            return null;
        });
    }

    default <P, E extends Exception> void closureOrConstantForEach(CheckedExceptionConsumer<T, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEach(forEach, (e, p) -> p.accept(e));
    }

    @FunctionalInterface
    public interface IObjectForEach2<T, P1, P2, E extends Exception> {

        void each(T element, P1 parameter1, P2 parameter2) throws E;
    }

    default <P1, P2, E extends Exception> void forEach(P1 parameter1, P2 parameter2, IObjectForEach2<T, P1, P2, E> forEach) throws E {

        forEachWithResult(null, parameter1, parameter2, (e, p1, p2) -> {

            forEach.each(e, p1, p2);

            return null;
        });
    }
}
