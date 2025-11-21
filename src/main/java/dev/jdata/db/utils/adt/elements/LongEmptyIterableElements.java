package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.function.CheckedExceptionLongConsumer;

abstract class LongEmptyIterableElements extends LongEmptyElements implements ILongIterableElements  {

    private static final long[] emptyArray = new long[0];

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);
    }

    @Override
    public final <E extends Exception> void closureOrConstantForEach(CheckedExceptionLongConsumer<E> forEach) throws E {

        Objects.requireNonNull(forEach);
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        return null;
    }

    @Override
    public final long[] toArray() {

        return emptyArray;
    }
}
