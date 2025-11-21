package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Predicate;

abstract class ObjectEmptyElements<T> extends EmptyElements implements IObjectElements<T> {

    @Override
    public final <P> boolean contains(P parameter, IObjectElementPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        return false;
    }

    @Override
    public final boolean closureOrConstantContains(Predicate<T> predicate) {

        Objects.requireNonNull(predicate);

        return false;
    }

    @Override
    public final boolean containsInstance(T instance) {

        Objects.requireNonNull(instance);

        return false;
    }
}
