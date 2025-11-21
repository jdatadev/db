package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.LongPredicate;

interface ILongByIndexOrderedElementsGetters extends IElementsGettersMarker {

    <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, ILongElementPredicate<P> predicate);

    default long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, LongPredicate predicate) {

        Objects.requireNonNull(predicate);

        return findAtMostOneIndexInRange(startIndex, numElements, predicate, (e, p) -> p.test(e));
    }
}
