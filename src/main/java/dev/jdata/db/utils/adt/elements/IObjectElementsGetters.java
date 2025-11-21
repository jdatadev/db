package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Predicate;

interface IObjectElementsGetters<T> extends IElementsGettersMarker {

    boolean contains(T value);
    <P> boolean contains(P parameter, IObjectElementPredicate<T, P> predicate);

    boolean containsOnly(T value);
    boolean containsOnly(T value, IObjectContainsOnlyPredicate<T> predicate);

    boolean containsInstance(T instance);

    default boolean closureOrConstantContains(Predicate<T> predicate) {

        Objects.requireNonNull(predicate);

        return contains(predicate, (e, p) -> p.test(e));
    }
}
