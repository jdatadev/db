package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

public interface IIntIterableElementsView extends IIntElementsView, IIntIterable {

    @Override
    default boolean contains(int value) {

        return forEachWithResult(Boolean.FALSE, Integer.valueOf(value), null, (e, v, p2) -> e == v.intValue() ? Boolean.TRUE : null);
    }

    @Override
    default <P> boolean contains(P parameter, IIntElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        return forEachWithResult(Boolean.FALSE, parameter, predicate, (element, passedParameer, passedPredicate) -> passedPredicate.test(element, passedParameer));
    }

    @Override
    default boolean containsOnly(int value) {

        return forEachWithResult(Boolean.TRUE, Integer.valueOf(value), null, (e, v, p2) -> e != v.intValue() ? Boolean.FALSE : null);
    }

    @Override
    default boolean containsOnly(int value, IIntContainsOnlyPredicate predicate) {

        return forEachWithResult(Boolean.TRUE, Integer.valueOf(value), predicate, (e, v, p) -> !p.test(v.intValue(), e) ? Boolean.FALSE : null);
    }
}
