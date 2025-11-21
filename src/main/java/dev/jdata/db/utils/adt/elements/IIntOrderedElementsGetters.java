package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.IntPredicate;

interface IIntOrderedElementsGetters extends IElementsGettersMarker {

    <P> long findAtMostOneIndex(P parameter, IIntElementPredicate<P> predicate);

    default long closureOrConstantFindAtMostOneIndex(IntPredicate predicate) {

        Objects.requireNonNull(predicate);

        return findAtMostOneIndex(predicate, (e, p) -> p.test(e));
    }
}
