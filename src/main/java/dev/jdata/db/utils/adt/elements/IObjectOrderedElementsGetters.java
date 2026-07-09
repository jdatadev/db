package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

interface IObjectOrderedElementsGetters<T> extends IElementsGettersMarker {

    <R> void map(IObjectOrderedAddable<R> addable, Function<T, R> mapper);

    <P> long findAtMostOneIndex(P parameter, BiPredicate<T, P> predicate);

    <P1, P2, E extends Exception> boolean equals(P1 thisParameter, IObjectOrderedElementsView<T> other, P2 otherParameter, IElementEqualityTester<T, P1, P2, E> equalityTester)
            throws E;

    default long closureOrConstantFindAtMostOneIndex(Predicate<T> predicate) {

        return findAtMostOneIndex(predicate, (e, p) -> p.test(e));
    }

    default <P> long findExactlyOneIndex(P parameter, BiPredicate<T, P> predicate) {

        final long foundIndex = findAtMostOneIndex(parameter, predicate);

        if (foundIndex == -1L) {

            throw ElementsExceptions.lessThanOneFoundException();
        }

        return foundIndex;
    }

    default long findInstanceIndex(T instance) {

        Objects.requireNonNull(instance);

        return findAtMostOneIndex(instance, (e, i) -> e == i);
    }

    default boolean equalsOrdered(IObjectOrderedElementsView<T> other) {

        Objects.requireNonNull(other);

        return equals(null, other, null, (e1, p1, e2, p2) -> e1.equals(e2));
    }
}
