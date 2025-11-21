package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

interface IObjectOrderedElementsGetters<T> extends IElementsGettersMarker {

    <R> void map(IObjectOrderedAddable<R> addable, Function<T, R> mapper);

    long closureOrConstantFindAtMostOneIndex(Predicate<T> predicate);
    <P> long findAtMostOneIndex(P parameter, BiPredicate<T, P> predicate);

    boolean equalsOrdered(IObjectOrderedElementsView<T> other);

    <P1, P2, E extends Exception> boolean equals(P1 thisParameter, IObjectOrderedElementsView<T> other, P2 otherParameter, IElementEqualityTester<T, P1, P2, E> equalityTester)
            throws E;

    default long findInstanceIndex(T instance) {

        Objects.requireNonNull(instance);

        return findAtMostOneIndex(instance, (e, i) -> e == i);
    }
}
