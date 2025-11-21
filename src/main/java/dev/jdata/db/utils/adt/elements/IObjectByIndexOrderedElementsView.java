package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.byindex.ByIndex;
import dev.jdata.db.utils.adt.byindex.IObjectByIndexView;
import dev.jdata.db.utils.checks.Checks;

public interface IObjectByIndexOrderedElementsView<T>

        extends IObjectOrderedElementsView<T>, IObjectByIndexView<T>, IByIndexOrderedElementsView, IObjectByIndexOrderedElementsGetters<T> {

    @Override
    default <P> long count(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        long count = 0;

        final long indexLimit = getIndexLimit();

        for (long i = 0L; i < indexLimit; ++ i) {

            if (predicate.test(get(i), parameter)) {

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

        final long indexLimit = getIndexLimit();

        for (long i = 0L; i < indexLimit; ++ i) {

            final int value = mapper.applyAsInt(get(i));

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

        final long indexLimit = getIndexLimit();

        for (long i = 0L; i < indexLimit; ++ i) {

            final long value = mapper.applyAsLong(get(i));

            if (value > max) {

                max = value;
                found = true;
            }
        }

        return found ? max : defaultValue;
    }

    @Override
    default <P, E extends Exception> void forEach(P parameter, IObjectForEach<T, P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        final long indexLimit = getIndexLimit();

        for (long i = 0L; i < indexLimit; ++ i) {

            forEach.each(get(i), parameter);
        }
    }

    @Override
    default <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IObjectForEachWithResult<T, P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        R result = defaultResult;

        final long indexLimit = getIndexLimit();

        for (long i = 0L; i < indexLimit; ++ i) {

            final R forEachResult = forEach.each(get(i), parameter1, parameter2);

            if (forEachResult != null) {

                result = forEachResult;
                break;
            }
        }

        return result;
    }

    @Override
    default <R> void map(IObjectOrderedAddable<R> addable, Function<T, R> mapper) {

        Objects.requireNonNull(addable);
        Objects.requireNonNull(mapper);

        final long indexLimit = getIndexLimit();

        for (long i = 0L; i < indexLimit; ++ i) {

            final R mapped = mapper.apply(get(i));

            addable.addTail(mapped);
        }
    }

    @Override
    default boolean containsInstance(T instance) {

        return ByIndex.containsInstance(this, getIndexLimit(), instance, IObjectByIndexView::get, IndexOutOfBoundsException::new);
    }

    @Override
    default boolean containsInstance(T instance, long startIndex, long numElements) {

        return ByIndex.containsInstance(this, getIndexLimit(), instance, startIndex, numElements, IObjectByIndexView::get, IndexOutOfBoundsException::new);
    }

    @Override
    default boolean closureOrConstantContains(Predicate<T> predicate) {

        return ByIndex.contains(this, getIndexLimit(), predicate, IObjectByIndexView::get, (e, p) -> p.test(e), IndexOutOfBoundsException::new);
    }

    @Override
    default <P> boolean contains(P parameter, IObjectElementPredicate<T, P> predicate) {

        return ByIndex.contains(this, getIndexLimit(), parameter, IObjectByIndexView::get, predicate, IndexOutOfBoundsException::new);
    }

    @Override
    default <P> boolean contains(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.contains(this, getIndexLimit(), startIndex, numElements, parameter, IObjectByIndexView::get, predicate, IndexOutOfBoundsException::new);
    }

    @Override
    default long closureOrConstantFindAtMostOneIndex(Predicate<T> predicate) {

        return ByIndex.findAtMostOneIndex(this, getIndexLimit(), predicate, IObjectByIndexView::get, (e, p) -> p.test(e), IndexOutOfBoundsException::new);
    }

    @Override
    default <P> long findAtMostOneIndex(P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.findAtMostOneIndex(this, getIndexLimit(), parameter, IObjectByIndexView::get, predicate, IndexOutOfBoundsException::new);
    }

    @Override
    default long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, Predicate<T> predicate) {

        return ByIndex.findAtMostOneIndex(this, getIndexLimit(), startIndex, numElements, predicate, IObjectByIndexView::get, predicate != null ? (e, p) -> p.test(e) : null,
                IndexOutOfBoundsException::new);
    }

    @Override
    default <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate) {

        return ByIndex.findAtMostOneIndex(this, getIndexLimit(), startIndex, numElements, parameter, IObjectByIndexView::get, predicate, IndexOutOfBoundsException::new);
    }

    @Override
    default boolean equalsOrdered(IObjectOrderedElementsView<T> other) {

        Objects.requireNonNull(other);

        final boolean equals;

        final long thisIndexLimit = getIndexLimit();

        if (other instanceof IObjectByIndexOrderedElementsView) {

            final IObjectByIndexOrderedElementsView<T> otherByIndexOrderedElementsView = (IObjectByIndexOrderedElementsView<T>)other;

            if (thisIndexLimit != otherByIndexOrderedElementsView.getIndexLimit()) {

                equals = false;
            }
            else {
                equals = equals(0L, otherByIndexOrderedElementsView, 0L, thisIndexLimit);
            }
        }
        else {
            throw new UnsupportedOperationException();
        }

        return equals;
    }

    @Override
    default boolean equals(long startIndex, IObjectByIndexOrderedElementsView<T> other, long otherStartIndex, long numElements) {

        Checks.checkFromIndexSize(startIndex, numElements, getIndexLimit());
        Objects.requireNonNull(other);
        Checks.checkFromIndexSize(otherStartIndex, numElements, other.getIndexLimit());

        boolean equals = false;

        for (long i = 0; i < numElements; ++ i) {

            if (!Objects.equals(get(startIndex + i), other.get(otherStartIndex + i))) {

                equals = false;
                break;
            }
        }

        return equals;
    }

    @Override
    default <P1, P2, E extends Exception> boolean equals(P1 thisParameter, IObjectOrderedElementsView<T> other, P2 otherParameter,
            IElementEqualityTester<T, P1, P2, E> equalityTester) throws E {

        final boolean equals;

        if (other instanceof IObjectByIndexOrderedElementsView) {

            final long thisIndexLimit = getIndexLimit();

            final IObjectByIndexOrderedElementsView<T> otherByIndexOrderedElementsView = (IObjectByIndexOrderedElementsView<T>)other;

            if (thisIndexLimit != otherByIndexOrderedElementsView.getIndexLimit()) {

                equals = false;
            }
            else {

                equals = equals(thisParameter, otherByIndexOrderedElementsView, otherParameter, equalityTester);
            }
        }
        else {
            throw new UnsupportedOperationException();
        }

        return equals;
    }

    @Override
    default <P1, P2, E extends Exception> boolean equals(long thisStartIndex, P1 thisParameter, IObjectByIndexOrderedElementsView<T> other, long otherStartIndex,
            P2 otherParameter, long numElements, IElementEqualityTester<T, P1, P2, E> equalityTester) throws E {

        Checks.checkLongIndex(thisStartIndex, getIndexLimit());
        Checks.checkLongIndex(otherStartIndex, other.getIndexLimit());

        return ByIndex.equals(this, thisStartIndex, thisParameter, other, otherStartIndex, otherParameter, numElements, equalityTester,
                (l1, i1, p1, l2, i2, p2, t) -> t.equals(l1.get(i1), p1, l2.get(i2), p2));
    }

    @Override
    default <P> void toString(StringBuilder sb, P parameter, IElementsToStringAdder<T, P> elementsToStringAdder) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(elementsToStringAdder);

        sb.append(ELEMENTS_TO_STRING_PREFIX);

        final long indexLimit = getIndexLimit();

        for (long i = 0; i < indexLimit; ++ i) {

            if (i > 0) {

                sb.append(ELEMENTS_TO_STRING_SEPARATOR);
            }

            elementsToStringAdder.addString(get(i), sb, parameter);
        }

        sb.append(ELEMENTS_TO_STRING_SUFFIX);
    }

    @Override
    default T[] toArray(IntFunction<T[]> createArray) {

        Objects.requireNonNull(createArray);

        return ObjectElementsHelper.toArrayIndexView(this, IByIndexOrderedElementsView.intIndexLimit(this), createArray);
    }
}
