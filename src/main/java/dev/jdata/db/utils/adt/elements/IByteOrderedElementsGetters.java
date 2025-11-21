package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.function.IBytePredicate;

interface IByteOrderedElementsGetters extends IElementsGettersMarker {

    <P> long findAtMostOneIndex(P parameter, IByteElementPredicate<P> predicate);

    default long closureOrConstantFindAtMostOneIndex(IBytePredicate predicate) {

        Objects.requireNonNull(predicate);

        return findAtMostOneIndex(predicate, (e, p) -> p.test(e));
    }
}
