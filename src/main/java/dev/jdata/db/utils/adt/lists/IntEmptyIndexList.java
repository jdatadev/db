package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntPredicate;

import dev.jdata.db.utils.adt.elements.IIntElementPredicate;
import dev.jdata.db.utils.checks.Checks;

abstract class IntEmptyIndexList extends IntEmptyList implements IIntIndexList {

    @Override
    public final int get(long index) {

        Checks.checkLongIndex(index, 0L);

        throw new NoSuchElementException();
    }

    @Override
    public final long getIndexLimit() {

        return 0L;
    }

    @Override
    public final <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, IIntElementPredicate<P> predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);
        Objects.requireNonNull(predicate);

        return -1L;
    }

    @Override
    public final long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, IntPredicate predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);
        Objects.requireNonNull(predicate);

        return -1L;
    }
}
