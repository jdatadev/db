package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.BiPredicate;

import dev.jdata.db.utils.adt.elements.ObjectEmptyOrderedElements;
import dev.jdata.db.utils.checks.Checks;

abstract class ObjectEmptyList<T> extends ObjectEmptyOrderedElements<T> implements IObjectListView<T> {

    @Override
    public final <P> boolean contains(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);

        return false;
    }

    @Override
    public final T getHead() {

        return null;
    }

    @Override
    public final T getTail() {

        return null;
    }

    @Override
    public final boolean equalsList(IObjectListView<T> other) {

        Objects.requireNonNull(other);

        return other.isEmpty();
    }
}
