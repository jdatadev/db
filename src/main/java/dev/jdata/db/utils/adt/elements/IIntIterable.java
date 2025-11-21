package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.function.CheckedExceptionIntConsumer;

interface IIntIterable extends IElementsIterable {

    <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, E> forEach) throws E;

    int[] toArray();

    default <P, E extends Exception> void forEach(P parameter, IIntForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEachWithResult(null, parameter, forEach, (e, p, f) -> {

            f.each(e, p);

            return null;
        });
    }

    default <E extends Exception> void closureOrConstantForEach(CheckedExceptionIntConsumer<E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEach(forEach, (e, f) -> f.accept(e));
    }
}
