package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.function.CheckedExceptionIntConsumer;

abstract class IntEmptyIterableElements extends IntEmptyElements implements IIntIterableElements  {

    private static final int[] emptyArray = new int[0];

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IIntForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);
    }

    @Override
    public final <E extends Exception> void closureOrConstantForEach(CheckedExceptionIntConsumer<E> forEach) throws E {

        Objects.requireNonNull(forEach);
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        return null;
    }

    @Override
    public final int[] toArray() {

        return emptyArray;
    }
}
