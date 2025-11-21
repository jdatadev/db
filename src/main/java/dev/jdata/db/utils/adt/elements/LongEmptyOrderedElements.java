package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.LongPredicate;

abstract class LongEmptyOrderedElements extends LongEmptyIterableElements implements ILongOrderedElementsView {

    @Override
    public final <P> long findAtMostOneIndex(P parameter, ILongElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        return -1L;
    }

    @Override
    public final long closureOrConstantFindAtMostOneIndex(LongPredicate predicate) {

        Objects.requireNonNull(predicate);

        return -1L;
    }
}
