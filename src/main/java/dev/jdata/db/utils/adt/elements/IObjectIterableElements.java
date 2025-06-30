package dev.jdata.db.utils.adt.elements;

import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.function.CheckedExceptionConsumer;

public interface IObjectIterableElements<T> extends IIterableElements {

    default long closureOrConstantCount(Predicate<T> predicate) {

        return count(predicate, (e, p) -> p.test(e));
    }

    <P> long count(P parameter, BiPredicate<T, P> predicate);

    int maxInt(int defaultValue, ToIntFunction<? super T> mapper);
    long maxLong(long defaultValue, ToLongFunction<? super T> mapper);

    T[] toArray(IntFunction<T[]> createArray);

    @FunctionalInterface
    public interface IForEach<T, P, E extends Exception> {

        void each(T element, P parameter) throws E;
    }

    <P, E extends Exception> void forEach(P parameter, IForEach<T, P, E> forEach) throws E;

    default <P, E extends Exception> void closureOrConstantForEach(CheckedExceptionConsumer<T, E> forEach) throws E {

        forEach(forEach, (e, p) -> p.accept(e));
    }

    @FunctionalInterface
    public interface IForEachWithResult<T, P1, P2, R, E extends Exception> {

        R each(T element, P1 parameter, P2 parameter2) throws E;
    }

    <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<T, P1, P2, R, E> forEach) throws E;

    <P> T findAtMostOne(P parameter, BiPredicate<T, P> predicate);
    <P> T findExactlyOne(P parameter, BiPredicate<T, P> predicate);

    default IndexList<T> sorted(Comparator<? super T> comparator, IndexListAllocator<T, ? extends IndexList<T>, ?, ?> indexListAllocator) {

        return IndexList.sortedOf(this, comparator, indexListAllocator);
    }
}
