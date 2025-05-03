package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public interface IIndexListGetters<T> extends IList<T>, ITailList<T> {

    T get(long index);

    @Override
    default <P, E extends Exception> void forEach(P parameter, ForEach<T, P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long numElements = getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            forEach.each(get(i), parameter);
        }
    }

    @Override
    default <P1, P2, E extends Exception> void forEach(P1 parameter1, P2 parameter2, ForEach2<T, P1, P2, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long numElements = getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            forEach.each(get(i), parameter1, parameter2);
        }
    }

    @Override
    default T getHead() {

        return get(0L);
    }

    @Override
    default T getTail() {

        return get(getNumElements() - 1L);
    }

    default long countWithClosure(Predicate<T> predicate) {

        return count(predicate, (e, p) -> p.test(e));
    }

    default <P> long count(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        long count = 0;

        final long numElements = getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            final T element = get(i);

            if (predicate.test(element, parameter)) {

                ++ count;
            }
        }

        return count;
    }

    @Override
    default int maxInt(int defaultValue, ToIntFunction<? super T> mapper) {

        Objects.requireNonNull(mapper);

        int max = Integer.MIN_VALUE;
        boolean found = false;

        final long numElements = getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            final T element = get(i);

            final int value = mapper.applyAsInt(element);

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }

    default long maxLong(long defaultValue, ToLongFunction<T> mapper) {

        Objects.requireNonNull(mapper);

        long max = Long.MIN_VALUE;
        boolean found = false;

        final long numElements = getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            final T element = get(i);

            final long value = mapper.applyAsLong(element);

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }
}
