package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

abstract class IntEmptyElements extends EmptyElements implements IIntElements {

    @Override
    public final boolean contains(int value) {

        return false;
    }

    @Override
    public final <P> boolean contains(P parameter, IIntElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        return false;
    }

    @Override
    public final boolean containsOnly(int value) {

        return false;
    }

    @Override
    public final boolean containsOnly(int value, IIntContainsOnlyPredicate predicate) {

        Objects.requireNonNull(predicate);

        return false;
    }
}
