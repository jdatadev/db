package dev.jdata.db.utils.adt.elements;

import java.util.Comparator;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.function.CheckedExceptionConsumer;

public interface IIterableElements<T> extends IElements {

    int maxInt(int defaultValue, ToIntFunction<? super T> mapper);

    T[] toArray(IntFunction<T[]> createArray);

    @FunctionalInterface
    public interface ForEach<T, P, E extends Exception> {

        void each(T element, P parameter) throws E;
    }

    <P, E extends Exception> void forEach(P parameter, ForEach<T, P, E> forEach) throws E;

    default <P, E extends Exception> void closureOrConstantForEach(CheckedExceptionConsumer<T, E> forEach) throws E {

        forEach(forEach, (e, p) -> p.accept(e));
    }

    @FunctionalInterface
    public interface ForEach2<T, P1, P2, E extends Exception> {

        void each(T element, P1 parameter, P2 parameter2) throws E;
    }

    <P1, P2, E extends Exception> void forEach(P1 parameter1, P2 parameter2, ForEach2<T, P1, P2, E> forEach) throws E;

    default IIndexList<T> sorted(Comparator<? super T> comparator, IntFunction<T[]> createArray, IndexListAllocator<T> allocator) {

        return IndexList.sortedOf(this, comparator, createArray, allocator);
    }
}
