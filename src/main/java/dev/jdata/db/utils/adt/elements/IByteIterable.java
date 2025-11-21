package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.function.CheckedExceptionByteConsumer;

interface IByteIterable extends IElementsIterable {

    <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IByteForEachWithResult<P1, P2, R, E> forEach) throws E;

    default <P, E extends Exception> void forEach(P parameter, IByteForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEachWithResult(null, parameter, forEach, (e, p, f) -> {

            f.each(e, p);

            return null;
        });
    }

    default <P, E extends Exception> void closureOrConstantForEach(CheckedExceptionByteConsumer<E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEach(forEach, (e, p) -> p.accept(e));
    }
}
