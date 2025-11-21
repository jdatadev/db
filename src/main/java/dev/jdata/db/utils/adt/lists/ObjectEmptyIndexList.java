package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import dev.jdata.db.utils.checks.Checks;

abstract class ObjectEmptyIndexList<T> extends ObjectEmptyList<T> implements IIndexList<T> {

    @Override
    public final <P> boolean contains(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);
        Objects.requireNonNull(predicate);

        return false;
    }

    @Override
    public final T get(long index) {

        Checks.checkLongIndex(index, 0L);

        throw new NoSuchElementException();
    }

    @Override
    public final long getIndexLimit() {

        return 0L;
    }

    @Override
    public final long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, Predicate<T> predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);
        Objects.requireNonNull(predicate);

        return -1L;
    }

    @Override
    public final <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, BiPredicate<T, P> predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);
        Objects.requireNonNull(predicate);

        return -1L;
    }
}
