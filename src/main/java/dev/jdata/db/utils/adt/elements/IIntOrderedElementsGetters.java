package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.IntPredicate;

interface IIntOrderedElementsGetters extends IElementsGettersMarker {

//    <R> void map(IObjectOrderedAddable<R> addable, Function<T, R> mapper);

    <P> long findAtMostOneIndex(P parameter, IIntElementPredicate<P> predicate);

    default long closureOrConstantFindAtMostOneIndex(IntPredicate predicate) {

        Objects.requireNonNull(predicate);

        return findAtMostOneIndex(predicate, (e, p) -> p.test(e));
    }
}
