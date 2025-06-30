package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CheckedExceptionLongConsumer;
import dev.jdata.db.utils.scalars.Integers;

public interface ILongIterableElements extends ILongElements, IIterableElements {

    @FunctionalInterface
    public interface IForEach<P, E extends Exception> {

        void each(long element, P parameter) throws E;
    }

    <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E;

    default <P, E extends Exception> void closureOrConstantForEach(CheckedExceptionLongConsumer<E> forEach) throws E {

        forEach(forEach, (e, p) -> p.accept(e));
    }

    @FunctionalInterface
    public interface IForEachWithResult<P1, P2, R, E extends Exception> {

        R each(long element, P1 parameter, P2 parameter2) throws E;
    }

    <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, E> forEach) throws E;

    @Override
    default boolean isEmpty() {

        return forEachWithResult(Boolean.TRUE, null, null, (e, p1, p2) -> Boolean.FALSE);
    }

    @Override
    default boolean contains(long value) {

        return forEachWithResult(Boolean.FALSE, value, null, (e, v, p2) -> e == v.longValue() ? Boolean.TRUE : null);
    }

    @Override
    default boolean containsOnly(long value) {

        Checks.isNotEmpty(this);

        return forEachWithResult(Boolean.TRUE, value, null, (e, v, p2) -> e != v.longValue() ? Boolean.FALSE : null);
    }

    @Override
    default boolean containsOnly(long value, IContainsOnlyPredicate predicate) {

        Checks.isNotEmpty(this);

        return forEachWithResult(Boolean.TRUE, value, predicate, (e, v, p) -> !p.test(v.longValue(), e) ? Boolean.FALSE : null);
    }

    @Override
    default long[] toArray() {

        final class ToArrayParameter {

            private final long[] result;

            private int index;

            ToArrayParameter(int numElements) {

                this.result = new long[numElements];

                this.index = 0;
            }
        }

        final ToArrayParameter toArrayParameter = new ToArrayParameter(Integers.checkUnsignedLongToUnsignedInt(getNumElements()));

        forEach(toArrayParameter, (e, p) -> p.result[p.index ++] = e);

        return toArrayParameter.result;
    }
}
