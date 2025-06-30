package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.adt.elements.ByIndex.IByIndexElementEqualityTester;
import dev.jdata.db.utils.adt.elements.IElementsToString;
import dev.jdata.db.utils.scalars.Integers;

public interface IIndexListGetters<T> extends IListGetters<T>, ITailListGetters<T>, IElementsToString<T> {

    @Override
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

    @Override
    default long maxLong(long defaultValue, ToLongFunction<? super T> mapper) {

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

    T get(long index);

    @Override
    default <P, E extends Exception> void forEach(P parameter, IForEach<T, P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long numElements = getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            forEach.each(get(i), parameter);
        }
    }

    @Override
    default <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<T, P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final long numElements = getNumElements();

        for (long i = 0L; i < numElements; ++ i) {

            final R forEachResult = forEach.each(get(i), parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    @Override
    default T[] toArray(IntFunction<T[]> createArray) {

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final T[] result = createArray.apply(numElements);

        for (int i = 0; i < numElements; ++ i) {

            result[i] = get(i);
        }

        return result;
    }

    @Override
    default T getHead() {

        return get(0L);
    }

    @Override
    default T getTail() {

        return get(getNumElements() - 1L);
    }

    @Override
    default <R> IIndexList<R> map(LongFunction<IIndexListBuildable<R, ?, ?>> createMappedList, Function<T, R> mapper) {

        Objects.requireNonNull(createMappedList);
        Objects.requireNonNull(mapper);

        final long numElements = getNumElements();

        final IIndexListBuildable<R, ?, ?> resultAddable = createMappedList.apply(numElements);

        for (long i = 0L; i < numElements; ++ i) {

            final R mapped = mapper.apply(get(i));

            resultAddable.addTail(mapped);
        }

        return resultAddable.build();
    }

    @Override
    default boolean containsInstance(T instance) {

        return ByIndex.containsInstance(this, getNumElements(), instance, IIndexListGetters::get, IndexOutOfBoundsException::new);
    }

    @Override
    default boolean containsInstance(T instance, long startIndex, long numElements) {

        return ByIndex.containsInstance(this, getNumElements(), instance, startIndex, numElements, IIndexListGetters::get, IndexOutOfBoundsException::new);
    }

    @Override
    default boolean closureOrConstantContains(Predicate<T> predicate) {

        return ByIndex.contains(this, getNumElements(), predicate, IIndexListGetters::get, (e, p) -> p.test(e), IndexOutOfBoundsException::new);
    }

    @Override
    default <P> boolean contains(P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.contains(this, getNumElements(), parameter, IIndexListGetters::get, predicate, IndexOutOfBoundsException::new);
    }

    @Override
    default <P> boolean contains(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.contains(this, getNumElements(), startIndex, numElements, parameter, IIndexListGetters::get, predicate, IndexOutOfBoundsException::new);
    }

    @Override
    default long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, Predicate<T> predicate) {

        return ByIndex.findAtMostOneIndex(this, getNumElements(), startIndex, numElements, predicate, IIndexListGetters::get, predicate != null ? (e, p) -> p.test(e) : null,
                IndexOutOfBoundsException::new);
    }

    @Override
    default <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.findAtMostOneIndex(this, getNumElements(), startIndex, numElements, parameter, IIndexListGetters::get, predicate, IndexOutOfBoundsException::new);
    }

    @Override
    default long closureOrConstantFindAtMostOneIndex(Predicate<T> predicate) {

        return ByIndex.findAtMostOneIndex(this, getNumElements(), predicate, IIndexListGetters::get, (e, p) -> p.test(e), IndexOutOfBoundsException::new);
    }

    @Override
    default <P> long findAtMostOneIndex(P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.findAtMostOneIndex(this, getNumElements(), parameter, IIndexListGetters::get, predicate, IndexOutOfBoundsException::new);
    }

    @FunctionalInterface
    public interface IIndexListEqualityTester<T, P1, P2> extends IByIndexElementEqualityTester<T, P1, P2> {

    }

    default <P1, P2> boolean equals(P1 thisParameter, IIndexListGetters<T> other, P2 otherParameter, IIndexListEqualityTester<T, P1, P2> equalityTester) {

        final boolean result;

        final long thisNumElements = getNumElements();

        if (thisNumElements == other.getNumElements()) {

            result = equals(0L, thisParameter, other, 0L, otherParameter, getNumElements(), equalityTester);
        }
        else {
            result = false;
        }

        return result;
    }

    default <P1, P2> boolean equals(long thisStartIndex, P1 thisParameter, IIndexListGetters<T> other, long otherStartIndex, P2 otherParameter, long numElements,
            IIndexListEqualityTester<T, P1, P2> equalityTester) {

        Objects.checkIndex(thisStartIndex, getNumElements());
        Objects.checkIndex(otherStartIndex, other.getNumElements());

        return ByIndex.equals(this, thisStartIndex, thisParameter, other, otherStartIndex, otherParameter, numElements, equalityTester,
                (l1, i1, p1, l2, i2, p2, d) -> d.equals(l1.get(i1), p1, l2.get(i2), p2));
    }
}

