package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CheckedExceptionIntConsumer;
import dev.jdata.db.utils.scalars.Integers;

public interface IIntIterableElements extends IIntElements, IIterableElements {

    @FunctionalInterface
    public interface IForEach<P, E extends Exception> {

        void each(int element, P parameter) throws E;
    }

    <P, E extends Exception> void forEach(P parameter, IForEach<P, E> forEach) throws E;

    default <P, E extends Exception> void closureOrConstantForEach(CheckedExceptionIntConsumer<E> forEach) throws E {

        forEach(forEach, (e, p) -> p.accept(e));
    }

    @FunctionalInterface
    public interface IForEachWithResult<P1, P2, R, E extends Exception> {

        R each(int element, P1 parameter, P2 parameter2) throws E;
    }

    <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, E> forEach) throws E;

    @Override
    default boolean isEmpty() {

        return forEachWithResult(Boolean.TRUE, null, null, (e, p1, p2) -> Boolean.FALSE);
    }

    @Override
    default boolean contains(int value) {

        return forEachWithResult(Boolean.FALSE, value, null, (e, v, p2) -> e == v.intValue() ? Boolean.TRUE : null);
    }

    @Override
    default boolean containsOnly(int value) {

        Checks.isNotEmpty(this);

        return forEachWithResult(Boolean.TRUE, value, null, (e, v, p2) -> e != v.intValue() ? Boolean.FALSE : null);
    }

    @Override
    default boolean containsOnly(int value, IContainsOnlyPredicate predicate) {

        Checks.isNotEmpty(this);

        return forEachWithResult(Boolean.TRUE, value, predicate, (e, v, p) -> !p.test(v.intValue(), e) ? Boolean.FALSE : null);
    }

    @Override
    default int[] toArray() {

        final class ToArrayParameter {

            private final int[] result;

            private int index;

            ToArrayParameter(int numElements) {

                this.result = new int[numElements];

                this.index = 0;
            }
        }

        final ToArrayParameter toArrayParameter = new ToArrayParameter(Integers.checkUnsignedLongToUnsignedInt(getNumElements()));

        forEach(toArrayParameter, (e, p) -> p.result[p.index ++] = e);

        return toArrayParameter.result;
    }
}
