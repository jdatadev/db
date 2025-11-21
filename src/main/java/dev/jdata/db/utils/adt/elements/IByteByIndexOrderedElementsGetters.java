package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.function.IBytePredicate;

interface IByteByIndexOrderedElementsGetters extends IElementsGettersMarker {

    <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, IByteElementPredicate<P> predicate);

    default long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, IBytePredicate predicate) {

        Objects.requireNonNull(predicate);

        return findAtMostOneIndexInRange(startIndex, numElements, predicate, (e, p) -> p.test(e));
    }
}
