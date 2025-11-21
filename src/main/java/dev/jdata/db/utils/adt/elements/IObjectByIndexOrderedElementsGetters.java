package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

interface IObjectByIndexOrderedElementsGetters<T> extends IElementsGettersMarker {

    <P> boolean contains(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate);

    boolean equals(long startIndex, IObjectByIndexOrderedElementsView<T> other, long otherStartIndex, long numElements);

    <P1, P2, E extends Exception> boolean equals(long thisStartIndex, P1 thisParameter, IObjectByIndexOrderedElementsView<T> other, long otherStartIndex,
            P2 otherParameter, long numElements, IElementEqualityTester<T, P1, P2, E> equalityTester) throws E;

    <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate);

    default long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, Predicate<T> predicate) {

        Objects.requireNonNull(predicate);

        return findAtMostOneIndexInRange(startIndex, numElements, predicate, (e, p) -> p.test(e));
    }
}
