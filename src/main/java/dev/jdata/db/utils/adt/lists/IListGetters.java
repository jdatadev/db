package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Predicate;

import dev.jdata.db.utils.adt.elements.IObjectIterableElements;

public interface IListGetters<T> extends IObjectIterableElements<T>, IHeadListGetters<T> {

    <R> IIndexList<R> map(LongFunction<IIndexListBuildable<R, ?, ?>> createMappedList, Function<T, R> mapper);

    boolean containsInstance(T instance);
    boolean containsInstance(T instance, long startIndex, long numElements);

    boolean closureOrConstantContains(Predicate<T> predicate);
    <P> boolean contains(P parameter, BiPredicate<T, P> predicate);

    <P> boolean contains(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate);

    long closureOrConstantFindAtMostOneIndex(Predicate<T> predicate);
    <P> long findAtMostOneIndex(P parameter, BiPredicate<T, P> predicate);

    long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, Predicate<T> predicate);
    <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate);

    default long findInstanceIndex(T instance) {

        Objects.requireNonNull(instance);

        return findAtMostOneIndex(instance, (e, i) -> e == i);
    }

    boolean equals(long startIndex, IListGetters<T> other, long otherStartIndex, long numElements);

    default boolean equalsList(IListGetters<T> other) {

        Objects.requireNonNull(other);

        final long numElements = getNumElements();

        boolean equals;

        if (numElements != other.getNumElements()) {

            equals = false;
        }
        else {
            equals = equals(0L, other, 0L, numElements);
        }

        return equals;
    }
}
