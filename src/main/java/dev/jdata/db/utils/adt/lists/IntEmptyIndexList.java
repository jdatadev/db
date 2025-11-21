package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntPredicate;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.IIntElementPredicate;
import dev.jdata.db.utils.checks.Checks;

abstract class IntEmptyIndexList extends IntEmptyList implements IIntIndexList {

    @Override
    public final int get(long index) {

        Checks.checkLongIndex(index, 0L);

        throw ElementsExceptions.emptyException();
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
