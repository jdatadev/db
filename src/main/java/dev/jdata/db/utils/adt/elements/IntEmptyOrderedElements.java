package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.IntPredicate;

abstract class IntEmptyOrderedElements extends IntEmptyIterableElements implements IIntOrderedElementsView {

    @Override
    public final <P> long findAtMostOneIndex(P parameter, IIntElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        return -1L;
    }

    @Override
    public final long closureOrConstantFindAtMostOneIndex(IntPredicate predicate) {

        Objects.requireNonNull(predicate);

        return -1L;
    }
}
