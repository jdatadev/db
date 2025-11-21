package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

abstract class LongEmptyElements extends EmptyElements implements ILongElements {

    @Override
    public final boolean contains(long value) {

        return false;
    }

    @Override
    public final <P> boolean contains(P parameter, ILongElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        return false;
    }

    @Override
    public final boolean containsOnly(long value) {

        return false;
    }

    @Override
    public final boolean containsOnly(long value, ILongContainsOnlyPredicate predicate) {

        Objects.requireNonNull(predicate);

        return false;
    }
}
