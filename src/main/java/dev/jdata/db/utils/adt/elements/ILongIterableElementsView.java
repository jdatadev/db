package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

public interface ILongIterableElementsView extends ILongElementsView, ILongIterable {

    @Override
    default boolean contains(long value) {

        return forEachWithResult(Boolean.FALSE, Long.valueOf(value), null, (e, v, p2) -> e == v.longValue() ? Boolean.TRUE : null);
    }

    @Override
    default <P> boolean contains(P parameter, ILongElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        return forEachWithResult(Boolean.FALSE, parameter, predicate, (element, passedParameer, passedPredicate) -> passedPredicate.test(element, passedParameer));
    }

    @Override
    default boolean containsOnly(long value) {

        return forEachWithResult(Boolean.TRUE, Long.valueOf(value), null, (e, v, p2) -> e != v.longValue() ? Boolean.FALSE : null);
    }

    @Override
    default boolean containsOnly(long value, ILongContainsOnlyPredicate predicate) {

        Objects.requireNonNull(predicate);

        return forEachWithResult(Boolean.TRUE, Long.valueOf(value), predicate, (e, v, p) -> !p.test(v.longValue(), e) ? Boolean.FALSE : null);
    }
}
