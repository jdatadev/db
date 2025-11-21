package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class ObjectEmptyOrderedElements<T> extends ObjectEmptyIterableElements<T> implements IObjectOrderedElementsView<T> {

    @Override
    public final <R> void map(IObjectOrderedAddable<R> addable, Function<T, R> mapper) {

        Objects.requireNonNull(addable);
        Objects.requireNonNull(mapper);
    }

    @Override
    public final long closureOrConstantFindAtMostOneIndex(Predicate<T> predicate) {

        Objects.requireNonNull(predicate);

        return -1L;
    }

    @Override
    public final <P> long findAtMostOneIndex(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return -1L;
    }
}
