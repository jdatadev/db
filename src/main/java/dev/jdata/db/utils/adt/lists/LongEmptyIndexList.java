package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.LongPredicate;

import dev.jdata.db.utils.adt.elements.ElementsExceptions;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;
import dev.jdata.db.utils.checks.Checks;

abstract class LongEmptyIndexList extends LongEmptyList implements ILongIndexList {

    @Override
    public final long get(long index) {

        Checks.checkLongIndex(index, 0L);

        throw ElementsExceptions.emptyException();
    }

    @Override
    public final long getIndexLimit() {

        return 0L;
    }

    @Override
    public final <P> long findAtMostOneIndexInRange(long startIndex, long numElements, P parameter, ILongElementPredicate<P> predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);
        Objects.requireNonNull(predicate);

        return -1L;
    }

    @Override
    public final long closureOrConstantFindAtMostOneIndexInRange(long startIndex, long numElements, LongPredicate predicate) {

        Checks.checkFromIndexSize(startIndex, numElements, 0L);
        Objects.requireNonNull(predicate);

        return -1L;
    }
}
