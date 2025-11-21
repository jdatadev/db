package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.IntPredicate;

interface IIntByIndexOrderedElementsGetters extends IElementsGettersMarker {

    <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, IIntElementPredicate<P> predicate);

    default long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, IntPredicate predicate) {

        Objects.requireNonNull(predicate);

        return findAtMostOneIndexInRange(startIndex, numElements, predicate, (e, p) -> p.test(e));
    }
}
