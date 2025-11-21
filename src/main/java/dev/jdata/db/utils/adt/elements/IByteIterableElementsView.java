package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

public interface IByteIterableElementsView extends IByteElementsView, IByteIterable {

    @Override
    default boolean contains(byte value) {

        return forEachWithResult(Boolean.FALSE, Long.valueOf(value), null, (e, v, p2) -> e == v.longValue() ? Boolean.TRUE : null);
    }

    @Override
    default <P> boolean contains(P parameter, IByteElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        return forEachWithResult(Boolean.FALSE, parameter, predicate, (element, passedParameer, passedPredicate) -> passedPredicate.test(element, passedParameer));
    }

    @Override
    default boolean containsOnly(byte value) {

        return forEachWithResult(Boolean.TRUE, Byte.valueOf(value), null, (e, v, p2) -> e != v.byteValue() ? Boolean.FALSE : null);
    }

    @Override
    default boolean containsOnly(byte value, IByteContainsOnlyPredicate predicate) {

        Objects.requireNonNull(predicate);

        return forEachWithResult(Boolean.TRUE, Byte.valueOf(value), predicate, (e, v, p) -> !p.test(v.byteValue(), e) ? Boolean.FALSE : null);
    }
}
