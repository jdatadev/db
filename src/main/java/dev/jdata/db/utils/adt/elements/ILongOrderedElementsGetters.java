package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.LongPredicate;

interface ILongOrderedElementsGetters extends IElementsGettersMarker {

    <P> long findAtMostOneIndex(P parameter, ILongElementPredicate<P> predicate);

    default long closureOrConstantFindAtMostOneIndex(LongPredicate predicate) {

        Objects.requireNonNull(predicate);

        return findAtMostOneIndex(predicate, (e, p) -> p.test(e));
    }
}
